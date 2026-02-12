import React, { useState } from 'react';
import './App.css';

function App() {
    const [showSignInModal, setShowSignInModal] = useState(false);
    const [showSignUpModal, setShowSignUpModal] = useState(false);
    const [showProfileDropdown, setShowProfileDropdown] = useState(false);
    const [message, setMessage] = useState('');
    const [user, setUser] = useState(null); // Store logged-in user
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    
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
                    setUser(instructor);
                    setIsLoggedIn(true);
                    setMessage(`Welcome back, ${instructor.firstName}!`);
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

    // Handle logout
    const handleLogout = () => {
        setUser(null);
        setIsLoggedIn(false);
        setMessage('');
        setShowProfileDropdown(false);
    };

    // Toggle profile dropdown
    const toggleProfileDropdown = () => {
        setShowProfileDropdown(!showProfileDropdown);
    };

    // Close dropdown when clicking outside
    const handleOutsideClick = (e) => {
        if (!e.target.closest('.profile-container')) {
            setShowProfileDropdown(false);
        }
    };

    // Add event listener for closing dropdown
    React.useEffect(() => {
        if (showProfileDropdown) {
            document.addEventListener('click', handleOutsideClick);
            return () => document.removeEventListener('click', handleOutsideClick);
        }
    }, [showProfileDropdown]);

    // Render login page
    if (!isLoggedIn) {
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

    // Render dashboard page after login
    return (
        <div className="dashboard">
            {/* Navigation Header */}
            <header className="navbar">
                <div className="navbar-content">
                    <div className="navbar-left">
                        <h1>Plagiarism Checker</h1>
                    </div>
                    <div className="navbar-right">
                        <div className="profile-container">
                            <button 
                                className="profile-icon" 
                                onClick={toggleProfileDropdown}
                                title="Profile"
                            >
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                                </svg>
                            </button>
                            
                            {showProfileDropdown && (
                                <div className="profile-dropdown">
                                    <div className="profile-info">
                                        <div className="profile-name">
                                            {user.firstName} {user.lastName}
                                        </div>
                                        <div className="profile-username">
                                            @{user.username}
                                        </div>
                                    </div>
                                    <div className="profile-actions">
                                        <button className="dropdown-item" onClick={handleLogout}>
                                            <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                                                <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.59L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z"/>
                                            </svg>
                                            Logout
                                        </button>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="main-content">
                <div className="welcome-section">
                    <h1>Welcome, {user.firstName}!</h1>
                    <p>You have successfully signed in to the Plagiarism Detection System</p>
                    
                    {message && (
                        <div className="message success">
                            {message}
                        </div>
                    )}

                    <div className="dashboard-cards">
                        <div className="card">
                            <h3>Upload Document</h3>
                            <p>Upload a document to check for plagiarism</p>
                            <button className="btn btn-primary">Upload</button>
                        </div>
                        
                        <div className="card">
                            <h3>View Reports</h3>
                            <p>View your previous plagiarism check reports</p>
                            <button className="btn btn-secondary">View Reports</button>
                        </div>
                        
                        <div className="card">
                            <h3>Settings</h3>
                            <p>Manage your account settings</p>
                            <button className="btn btn-secondary">Settings</button>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
}

export default App;