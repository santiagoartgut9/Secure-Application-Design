const API_BASE = "/api";

document.getElementById('btnReg').addEventListener('click', register);
document.getElementById('btnLog').addEventListener('click', login);
document.getElementById('btnApi').addEventListener('click', callApi);

async function register(){
  const username = document.getElementById('regUser').value;
  const password = document.getElementById('regPass').value;
  const r = await fetch(`${API_BASE}/auth/register`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({username, password})
  });
  document.getElementById('output').textContent = await r.text();
}

async function login(){
  const username = document.getElementById('logUser').value;
  const password = document.getElementById('logPass').value;
  const r = await fetch(`${API_BASE}/auth/login`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({username, password})
  });
  const json = await r.json();
  if (json.token) localStorage.setItem('jwt', json.token);
  document.getElementById('output').textContent = JSON.stringify(json, null, 2);
}

async function callApi(){
  const token = localStorage.getItem('jwt');
  const r = await fetch(`${API_BASE}/hello`, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  document.getElementById('output').textContent = await r.text();
}
