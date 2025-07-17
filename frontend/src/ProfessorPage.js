// src/ProfessorPage.js
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function ProfessorPage() {
  const { username } = useParams();
  const [professor, setProfessor] = useState(null);

  const API = 'http://127.0.0.1:8000';

  useEffect(() => {
    const fetchProfessor = async () => {
      try {
        const res = await axios.get(`${API}/professors/${username}`);
        setProfessor(res.data);
      } catch (err) {
        alert("Failed to load professor data");
      }
    };
    fetchProfessor();
  }, [username]);

  if (!professor) return <p>Loading...</p>;

  return (
    <div style={{ padding: 20 }}>
      <h2>Welcome, {professor.name}</h2>
      <p>Email: {professor.email}</p>
      <p>Username: {professor.username}</p>
      {/* You can add course list or more options here */}
    </div>
  );
}

export default ProfessorPage;
