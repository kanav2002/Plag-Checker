// src/HomePage.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function HomePage() {
  const [professors, setProfessors] = useState([]);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [newProf, setNewProf] = useState({ username: '', password: '', name: '', email: '' });
  const navigate = useNavigate();

  const API = 'http://127.0.0.1:8000';

  const getProfessors = async () => {
    const res = await axios.get(`${API}/professors/`);
    setProfessors(res.data);
  };

  const createProfessor = async () => {
    await axios.post(`${API}/professors/`, newProf);
    alert('Professor created');
  };

  const deleteProfessor = async () => {
    await axios.delete(`${API}/professors/${username}`);
    alert('Professor deleted');
  };

  const login = async () => {
    try {
      const formData = new FormData();
      formData.append('username', username);
      formData.append('password', password);
      await axios.post(`${API}/login`, formData);
      navigate(`/professor/${username}`);  // Redirect after login
    } catch (err) {
      if (err.response?.status === 401) {
        alert("Invalid password");
      } else if (err.response?.status === 404) {
        alert("User not found");
      } else {
        alert("Login failed");
      }
    }
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>Welcome to Plagiarism Checker</h1>

      <button onClick={getProfessors}>Get Professors</button>
      <ul>
        {professors.map((p) => (
          <li key={p.id}>{p.name} ({p.username})</li>
        ))}
      </ul>

      <h3>Create Professor</h3>
      <input placeholder="Username" onChange={(e) => setNewProf({ ...newProf, username: e.target.value })} />
      <input placeholder="Password" type="password" onChange={(e) => setNewProf({ ...newProf, password: e.target.value })} />
      <input placeholder="Name" onChange={(e) => setNewProf({ ...newProf, name: e.target.value })} />
      <input placeholder="Email" onChange={(e) => setNewProf({ ...newProf, email: e.target.value })} />
      <button onClick={createProfessor}>Submit</button>

      <h3>Delete Professor</h3>
      <input placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
      <button onClick={deleteProfessor}>Delete</button>

      <h3>Login</h3>
      <input placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
      <input placeholder="Password" type="password" onChange={(e) => setPassword(e.target.value)} />
      <button onClick={login}>Login</button>
    </div>
  );
}

export default HomePage;
