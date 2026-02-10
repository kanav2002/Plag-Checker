import React, { useState } from 'react';

function App() {
    const [response, setResponse] = useState('');

    const handleClick = async () => {
        try {
            const res = await fetch('http://localhost:8080/api/hello');
            const data = await res.text();
            setResponse(data);
        } catch (error) {
            setResponse(`Error: ${error.message}`);
        }
    };

    return (
        <div>
            <h1>Plagiarism Checker</h1>
            <button onClick={handleClick}>Say Hello</button>
            <div>{response && <h2>{response}</h2>}</div>
        </div>
    );
}

export default App;