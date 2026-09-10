import './style.css'

type Resource = 'students' | 'faculty' | 'workers'
type Student = { studentId: string; studentName: string; studentAddress: string; studentPhonenumber: string }
type Faculty = { facultyId: number; facultyName: string; subject: string; salary: number }
type Worker = { workerId: number; workerName: string; profession: string; salary: number }
type RecordItem = Student | Faculty | Worker
type AuthUser = { id: number; name: string; email: string; provider: string }
type AuthResponse = { token: string; user: AuthUser }
type ResourceConfig = { label: string; singular: string; endpoint: string; id: (record: RecordItem) => string | number; fields: Array<{ key: string; label: string; type?: 'text' | 'number' | 'tel' }> }

const apiUrl = (import.meta.env.VITE_API_URL ?? 'http://localhost:8081').replace(/\/$/, '')
const tokenKey = 'campusflow_token'
const resources: Record<Resource, ResourceConfig> = {
  students: { label: 'Students', singular: 'student', endpoint: '/studentdetails', id: (record) => (record as Student).studentId, fields: [{ key: 'studentId', label: 'Student ID' }, { key: 'studentName', label: 'Student name' }, { key: 'studentAddress', label: 'Address' }, { key: 'studentPhonenumber', label: 'Phone number', type: 'tel' }] },
  faculty: { label: 'Faculty', singular: 'faculty member', endpoint: '/facultydetails', id: (record) => (record as Faculty).facultyId, fields: [{ key: 'facultyName', label: 'Faculty name' }, { key: 'subject', label: 'Subject' }, { key: 'salary', label: 'Annual salary', type: 'number' }] },
  workers: { label: 'Workers', singular: 'worker', endpoint: '/workersdetails', id: (record) => (record as Worker).workerId, fields: [{ key: 'workerName', label: 'Worker name' }, { key: 'profession', label: 'Profession' }, { key: 'salary', label: 'Annual salary', type: 'number' }] },
}
const state: { active: Resource; records: Record<Resource, RecordItem[]>; loading: boolean; user: AuthUser | null; googleEnabled: boolean; authMode: 'login' | 'register' } = {
  active: 'students', records: { students: [], faculty: [], workers: [] }, loading: false, user: null, googleEnabled: false, authMode: 'login',
}
const app = document.querySelector<HTMLDivElement>('#app')!

function escapeHtml(value: unknown): string { return String(value ?? '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#039;') }
function titleCase(value: string): string { return value.replace(/([A-Z])/g, ' $1').replace(/^./, (letter) => letter.toUpperCase()) }
function formatValue(key: string, value: unknown): string { return key === 'salary' && typeof value === 'number' ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(value) : escapeHtml(value) }
function storedToken(): string | null { return window.localStorage.getItem(tokenKey) }

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const headers = new Headers(options?.headers)
  if (options?.body && !headers.has('Content-Type')) headers.set('Content-Type', 'application/json')
  const token = storedToken()
  if (token) headers.set('Authorization', `Bearer ${token}`)
  const response = await fetch(`${apiUrl}${path}`, { ...options, headers })
  if (!response.ok) {
    let message = await response.text()
    try { message = (JSON.parse(message) as { message?: string }).message ?? message } catch { /* text response */ }
    throw new Error(message || `Request failed (${response.status})`)
  }
  const contentType = response.headers.get('content-type') ?? ''
  return (contentType.includes('application/json') ? response.json() : response.text()) as Promise<T>
}

function showToast(message: string, kind: 'success' | 'error' = 'success'): void {
  const toast = document.createElement('div')
  toast.className = `toast ${kind}`
  toast.setAttribute('role', 'status')
  toast.textContent = message
  document.body.append(toast)
  window.setTimeout(() => toast.remove(), 3600)
}

function authScreen(): string {
  const register = state.authMode === 'register'
  return `<main class="auth-layout"><section class="auth-aside"><a class="brand" href="#"><span class="brand-mark">U</span><span>Campus<span class="brand-accent">Flow</span></span></a><div><p class="eyebrow">University management system</p><h1>Everything on campus, in one place.</h1><p class="auth-copy">Securely manage student, faculty, and worker records from a single workspace.</p></div><p class="auth-footnote">Built for your university team.</p></section><section class="auth-panel"><div class="auth-card"><p class="eyebrow">Welcome to CampusFlow</p><h2>${register ? 'Create your account' : 'Sign in to your account'}</h2><p class="auth-subtitle">${register ? 'Start managing your university records securely.' : 'Use your account to continue to the dashboard.'}</p><div class="auth-tabs"><button class="${!register ? 'active' : ''}" data-auth-mode="login">Sign in</button><button class="${register ? 'active' : ''}" data-auth-mode="register">Register</button></div><form id="auth-form">${register ? '<label>Full name<input name="name" type="text" autocomplete="name" required></label>' : ''}<label>Email address<input name="email" type="email" autocomplete="email" required></label><label>Password<input name="password" type="password" autocomplete="${register ? 'new-password' : 'current-password'}" minlength="8" required></label>${register ? '<small class="field-note">Use at least 8 characters.</small>' : ''}<p class="form-error" id="auth-error" role="alert"></p><button class="primary-button auth-submit" type="submit">${register ? 'Create account' : 'Sign in'}</button></form><div class="divider"><span>or</span></div><button class="google-button" id="google-sign-in" ${state.googleEnabled ? '' : 'disabled'}><span class="google-g">G</span> Continue with Google</button>${state.googleEnabled ? '' : '<p class="google-note">Google sign-in is not configured yet.</p>'}</div></section></main>`
}

function dashboard(): string {
  const config = resources[state.active]
  const records = state.records[state.active]
  const headers = config.fields.map((field) => `<th>${field.label}</th>`).join('')
  const rows = records.map((record) => `<tr>${config.fields.map((field) => `<td>${formatValue(field.key, (record as Record<string, unknown>)[field.key])}</td>`).join('')}<td class="actions"><button class="icon-button" data-action="edit" data-id="${escapeHtml(config.id(record))}">Edit</button><button class="icon-button danger" data-action="delete" data-id="${escapeHtml(config.id(record))}">Delete</button></td></tr>`).join('')
  return `<main class="shell"><aside class="sidebar"><a class="brand" href="#"><span class="brand-mark">U</span><span>Campus<span class="brand-accent">Flow</span></span></a><p class="sidebar-label">Manage records</p><nav class="nav">${Object.entries(resources).map(([key, resource]) => `<button class="nav-item ${state.active === key ? 'active' : ''}" data-resource="${key}"><span class="nav-icon">${key === 'students' ? 'S' : key === 'faculty' ? 'F' : 'W'}</span>${resource.label}<span class="nav-count">${state.records[key as Resource].length}</span></button>`).join('')}</nav><div class="sidebar-user"><span class="user-avatar">${escapeHtml(state.user?.name.charAt(0).toUpperCase())}</span><span><strong>${escapeHtml(state.user?.name)}</strong><small>${escapeHtml(state.user?.email)}</small></span><button id="logout">Sign out</button></div></aside><section class="content"><header class="topbar"><div><p class="eyebrow">University management system</p><h1>${config.label}</h1></div><button class="primary-button" id="new-record">+ Add ${config.singular}</button></header><section class="stat-grid">${Object.entries(resources).map(([key, resource]) => `<article class="stat-card ${state.active === key ? 'selected' : ''}"><span>${resource.label}</span><strong>${state.records[key as Resource].length}</strong><small>total records</small></article>`).join('')}</section><section class="panel"><div class="panel-toolbar"><div><h2>All ${config.label.toLowerCase()}</h2><p>${records.length} record${records.length === 1 ? '' : 's'} available</p></div><div class="toolbar-actions"><label class="search"><span>Search</span><input id="search" type="search" placeholder="Search ${config.label.toLowerCase()}"></label><button class="secondary-button" id="reload" ${state.loading ? 'disabled' : ''}>${state.loading ? 'Loading...' : 'Refresh'}</button></div></div><div class="table-wrap"><table><thead><tr>${headers}<th>Actions</th></tr></thead><tbody id="records-body">${rows || `<tr><td class="empty" colspan="${config.fields.length + 1}"><strong>No ${config.label.toLowerCase()} yet</strong><span>Add the first ${config.singular} to get started.</span></td></tr>`}</tbody></table></div></section></section></main>`
}

function render(): void { app.innerHTML = state.user ? dashboard() : authScreen(); if (state.user) bindDashboardEvents(); else bindAuthEvents() }
function bindAuthEvents(): void {
  document.querySelectorAll<HTMLButtonElement>('[data-auth-mode]').forEach((button) => button.addEventListener('click', () => { state.authMode = button.dataset.authMode as 'login' | 'register'; render() }))
  document.querySelector('#google-sign-in')?.addEventListener('click', () => { window.location.assign(`${apiUrl}/oauth2/authorization/google`) })
  document.querySelector<HTMLFormElement>('#auth-form')?.addEventListener('submit', async (event) => {
    event.preventDefault(); const form = event.currentTarget as HTMLFormElement; if (!form.checkValidity()) { form.reportValidity(); return }
    const button = form.querySelector<HTMLButtonElement>('button[type="submit"]')!; const error = document.querySelector<HTMLParagraphElement>('#auth-error')!; const values = Object.fromEntries(new FormData(form).entries())
    button.disabled = true; button.textContent = 'Please wait...'; error.textContent = ''
    try { const endpoint = state.authMode === 'register' ? '/auth/register' : '/auth/login'; const response = await request<AuthResponse>(endpoint, { method: 'POST', body: JSON.stringify(values) }); window.localStorage.setItem(tokenKey, response.token); state.user = response.user; render(); void loadAll() } catch (exception) { error.textContent = exception instanceof Error ? exception.message : 'Unable to sign in.'; button.disabled = false; button.textContent = state.authMode === 'register' ? 'Create account' : 'Sign in' }
  })
}
function bindDashboardEvents(): void {
  document.querySelectorAll<HTMLButtonElement>('[data-resource]').forEach((button) => button.addEventListener('click', () => { state.active = button.dataset.resource as Resource; render() }))
  document.querySelector('#new-record')?.addEventListener('click', () => openForm())
  document.querySelector('#reload')?.addEventListener('click', () => void loadAll())
  document.querySelector('#search')?.addEventListener('input', filterRows)
  document.querySelector('#logout')?.addEventListener('click', logout)
  document.querySelectorAll<HTMLButtonElement>('[data-action]').forEach((button) => { const id = button.dataset.id!; button.addEventListener('click', () => { if (button.dataset.action === 'edit') openForm(id); else void deleteRecord(id) }) })
}
function filterRows(): void { const term = (document.querySelector<HTMLInputElement>('#search')?.value ?? '').trim().toLowerCase(); document.querySelectorAll<HTMLTableRowElement>('#records-body tr').forEach((row) => { row.hidden = term.length > 0 && !row.textContent!.toLowerCase().includes(term) }) }
function openForm(id?: string): void {
  const config = resources[state.active]; const record = id === undefined ? undefined : state.records[state.active].find((item) => String(config.id(item)) === id) as Record<string, unknown> | undefined; const isEdit = record !== undefined
  const fields = config.fields.map((field) => { const value = record?.[field.key] ?? ''; const idField = field.key.toLowerCase().endsWith('id'); return `<label>${field.label}<input name="${field.key}" type="${field.type ?? 'text'}" value="${escapeHtml(value)}" ${idField && isEdit ? 'readonly' : ''} required></label>` }).join('')
  const dialog = document.createElement('dialog'); dialog.className = 'record-dialog'; dialog.innerHTML = `<form method="dialog" id="record-form"><div class="dialog-header"><div><p class="eyebrow">${isEdit ? 'Update record' : 'New record'}</p><h2>${isEdit ? `Edit ${config.singular}` : `Add ${config.singular}`}</h2></div><button class="close-button" value="cancel">x</button></div><div class="form-grid">${fields}</div><p class="form-error" id="form-error" role="alert"></p><div class="dialog-actions"><button class="secondary-button" value="cancel">Cancel</button><button class="primary-button" value="default" id="save-record">${isEdit ? 'Save changes' : `Add ${config.singular}`}</button></div></form>`; document.body.append(dialog); dialog.showModal(); dialog.addEventListener('close', () => dialog.remove())
  dialog.querySelector<HTMLFormElement>('#record-form')!.addEventListener('submit', async (event) => { event.preventDefault(); const form = event.currentTarget as HTMLFormElement; if (!form.checkValidity()) { form.reportValidity(); return }; const values = Object.fromEntries(new FormData(form).entries()) as Record<string, string>; for (const field of config.fields) if (field.type === 'number') values[field.key] = String(Number(values[field.key])); const saveButton = dialog.querySelector<HTMLButtonElement>('#save-record')!; saveButton.disabled = true; saveButton.textContent = 'Saving...'; try { await request(`${config.endpoint}${isEdit ? `/${encodeURIComponent(id!)}` : ''}`, { method: isEdit ? 'PUT' : 'POST', body: JSON.stringify(values) }); dialog.close(); showToast(`${titleCase(config.singular)} ${isEdit ? 'updated' : 'created'} successfully.`); await loadAll() } catch (exception) { dialog.querySelector('#form-error')!.textContent = exception instanceof Error ? exception.message : 'Unable to save this record.'; saveButton.disabled = false; saveButton.textContent = isEdit ? 'Save changes' : `Add ${config.singular}` } })
}
async function deleteRecord(id: string): Promise<void> { const config = resources[state.active]; if (!window.confirm(`Delete this ${config.singular}? This action cannot be undone.`)) return; try { await request(`${config.endpoint}/${encodeURIComponent(id)}`, { method: 'DELETE' }); showToast(`${titleCase(config.singular)} deleted successfully.`); await loadAll() } catch (exception) { showToast(exception instanceof Error ? exception.message : 'Unable to delete this record.', 'error') } }
async function loadAll(): Promise<void> { if (!state.user) return; state.loading = true; render(); try { const responses = await Promise.all((Object.keys(resources) as Resource[]).map(async (key) => [key, await request<RecordItem[]>(resources[key].endpoint)] as const)); for (const [key, records] of responses) state.records[key] = records } catch (exception) { showToast(exception instanceof Error ? `Could not load records: ${exception.message}` : 'Could not load records.', 'error') } finally { state.loading = false; render() } }
function logout(): void { window.localStorage.removeItem(tokenKey); state.user = null; state.records = { students: [], faculty: [], workers: [] }; render() }
async function bootstrap(): Promise<void> { const params = new URLSearchParams(window.location.search); const oauthToken = params.get('token'); if (oauthToken) { window.localStorage.setItem(tokenKey, oauthToken); window.history.replaceState({}, document.title, window.location.pathname) }; try { state.googleEnabled = (await request<{ enabled: boolean }>('/auth/google-enabled')).enabled } catch { state.googleEnabled = false }; if (!storedToken()) { render(); return }; try { state.user = await request<AuthUser>('/auth/me'); render(); await loadAll() } catch { logout() } }
void bootstrap()
