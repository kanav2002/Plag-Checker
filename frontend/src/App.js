// src/App.js
import React, { useState } from 'react';
import axios from 'axios';

function App() {
  const [professors, setProfessors] = useState([]);
  const [courses, setCourses] = useState([]);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loggedInProf, setLoggedInProf] = useState(null);
  const [newProf, setNewProf] = useState({ username: '', password: '', name: '', email: '' });
  const [newCourse, setNewCourse] = useState({ code: '', name: '', professor_id: '' });

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

  const getCourses = async () => {
    const res = await axios.get(`${API}/courses/`);
    setCourses(res.data);
  };

  const createCourse = async () => {
    await axios.post(`${API}/courses/`, newCourse);
    alert('Course created');
  };

  const login = async () => {
    try {
      const res = await axios.get(`${API}/professors/${username}`);
      if (res.data.password === password) {
        setLoggedInProf(res.data);
      } else {
        alert('Invalid password');
      }
    } catch (err) {
      alert('Login failed');
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

      <h3>Get Courses</h3>
      <button onClick={getCourses}>Get</button>
      <ul>
        {courses.map((c) => (
          <li key={c.id}>{c.name} ({c.code}) taught by {c.professor_name}</li>
        ))}
      </ul>

      <h3>Create Course</h3>
      <input placeholder="Code" onChange={(e) => setNewCourse({ ...newCourse, code: e.target.value })} />
      <input placeholder="Name" onChange={(e) => setNewCourse({ ...newCourse, name: e.target.value })} />
      <input placeholder="Professor ID" onChange={(e) => setNewCourse({ ...newCourse, professor_id: e.target.value })} />
      <button onClick={createCourse}>Create</button>

      <h3>Login</h3>
      <input placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
      <input placeholder="Password" type="password" onChange={(e) => setPassword(e.target.value)} />
      <button onClick={login}>Login</button>

      {loggedInProf && (
        <div>
          <h2>Welcome, {loggedInProf.name}</h2>
          <p>Email: {loggedInProf.email}</p>
          <h4>Your Courses:</h4>
          <button onClick={async () => {
            const res = await axios.get(`${API}/professors/${loggedInProf.id}/courses`);
            setCourses(res.data);
          }}>Get My Courses</button>
          <ul>
            {courses.map((c) => (
              <li key={c.id}>{c.name} ({c.code})</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default App;
