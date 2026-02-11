import React, { useState } from 'react';
import './App.css';

function App() {
    const [showSignInModal, setShowSignInModal] = useState(false);
    const [showSignUpModal, setShowSignUpModal] = useState(false);
    const [message, setMessage] = useState('');
    
    // Sign In form state
    const [signInData, setSignInData] = useState({
        username: '',
        password: ''
    });
    
    // Sign Up form state
    const [signUpData, setSignUpData] = useState({
        username: '',
        password: '',
        firstName: '',
        lastName: ''
    });

    // Handle Sign In
    const handleSignIn = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/instructors/username/${signInData.username}`);
            
            if (response.ok) {
                const instructor = await response.json();
                if (instructor.password === signInData.password) {
                    setMessage('Congratulations, you are a member!');
                } else {
                    setMessage('Wrong username or password');
                }
            } else {
                setMessage('Wrong username or password');
            }
        } catch (error) {
            setMessage('Error connecting to server');
        }
        
        setShowSignInModal(false);
        setSignInData({ username: '', password: '' });
    };

    // Handle Sign Up
    const handleSignUp = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/instructors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(signUpData)
            });

            if (response.ok) {
                const newInstructor = await response.json();
                setMessage(`Welcome ${newInstructor.firstName}! Your account has been created successfully.`);
            } else {
                setMessage('Error creating account. Username might already exist.');
            }
        } catch (error) {
            setMessage('Error connecting to server');
        }
        
        setShowSignUpModal(false);
        setSignUpData({ username: '', password: '', firstName: '', lastName: '' });
    };

    // Handle input changes for Sign In
    const handleSignInChange = (e) => {
        setSignInData({
            ...signInData,
            [e.target.name]: e.target.value
        });
    };

    // Handle input changes for Sign Up
    const handleSignUpChange = (e) => {
        setSignUpData({
            ...signUpData,
            [e.target.name]: e.target.value
        });
    };

    // Close modals
    const closeModals = () => {
        setShowSignInModal(false);
        setShowSignUpModal(false);
        setSignInData({ username: '', password: '' });
        setSignUpData({ username: '', password: '', firstName: '', lastName: '' });
    };

    return (
        <div className="app">
            <div className="container">
                <h1>Plagiarism Checker</h1>
                <p>Welcome to our plagiarism detection system</p>
                
                <div className="button-container">
                    <button 
                        className="btn btn-primary" 
                        onClick={() => setShowSignInModal(true)}
                    >
                        Sign In
                    </button>
                    <button 
                        className="btn btn-secondary" 
                        onClick={() => setShowSignUpModal(true)}
                    >
                        Sign Up
                    </button>
                </div>

                {message && (
                    <div className={`message ${message.includes('Congratulations') || message.includes('Welcome') ? 'success' : 'error'}`}>
                        {message}
                    </div>
                )}
            </div>

            {/* Sign In Modal */}
            {showSignInModal && (
                <div className="modal-overlay" onClick={closeModals}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>Sign In</h2>
                            <button className="close-btn" onClick={closeModals}>&times;</button>
                        </div>
                        <form onSubmit={handleSignIn}>
                            <div className="form-group">
                                <label htmlFor="signInUsername">Username:</label>
                                <input
                                    type="text"
                                    id="signInUsername"
                                    name="username"
                                    value={signInData.username}
                                    onChange={handleSignInChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="signInPassword">Password:</label>
                                <input
                                    type="password"
                                    id="signInPassword"
                                    name="password"
                                    value={signInData.password}
                                    onChange={handleSignInChange}
                                    required
                                />
                            </div>
                            <div className="form-actions">
                                <button type="button" className="btn btn-cancel" onClick={closeModals}>
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-primary">
                                    Sign In
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {/* Sign Up Modal */}
            {showSignUpModal && (
                <div className="modal-overlay" onClick={closeModals}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>Sign Up</h2>
                            <button className="close-btn" onClick={closeModals}>&times;</button>
                        </div>
                        <form onSubmit={handleSignUp}>
                            <div className="form-group">
                                <label htmlFor="signUpUsername">Username:</label>
                                <input
                                    type="text"
                                    id="signUpUsername"
                                    name="username"
                                    value={signUpData.username}
                                    onChange={handleSignUpChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="signUpPassword">Password:</label>
                                <input
                                    type="password"
                                    id="signUpPassword"
                                    name="password"
                                    value={signUpData.password}
                                    onChange={handleSignUpChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="firstName">First Name:</label>
                                <input
                                    type="text"
                                    id="firstName"
                                    name="firstName"
                                    value={signUpData.firstName}
                                    onChange={handleSignUpChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="lastName">Last Name:</label>
                                <input
                                    type="text"
                                    id="lastName"
                                    name="lastName"
                                    value={signUpData.lastName}
                                    onChange={handleSignUpChange}
                                    required
                                />
                            </div>
                            <div className="form-actions">
                                <button type="button" className="btn btn-cancel" onClick={closeModals}>
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-primary">
                                    Sign Up
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default App;