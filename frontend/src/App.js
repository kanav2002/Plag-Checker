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

    const [showSettingsModal, setShowSettingsModal] = useState(false);
    const [showSuccessToast, setShowSuccessToast] = useState(false);
    const [passwordData, setPasswordData] = useState({
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
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

    const handlePasswordChange = async (e) => {
        e.preventDefault();
        if (passwordData.newPassword !== passwordData.confirmPassword) {
            setMessage('New passwords do not match');
            return;
        }
        
        try {
            const response = await fetch(`http://localhost:8080/api/instructors/password/${user.username}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    oldPassword: passwordData.oldPassword,
                    newPassword: passwordData.newPassword
                })
            });

            if (response.ok) {
                setShowSettingsModal(false);
                setShowSuccessToast(true);
                // Clear password form
                setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
                
                setTimeout(() => {
                    setShowSuccessToast(false);
                    // Clear message and reset states before logout
                    setMessage('');
                    setShowProfileDropdown(false);
                    handleLogout();
                }, 2000);
            } else {
                setMessage('Invalid old password');
            }
        } catch (error) {
            setMessage('Error updating password');
        }
    };

    const handlePasswordInputChange = (e) => {
        setPasswordData({
            ...passwordData,
            [e.target.name]: e.target.value
        });
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

    if (!user) {
        return null;
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
                                        <button className="dropdown-item" onClick={() => {
                                            setShowProfileDropdown(false);
                                            setShowSettingsModal(true);
                                        }}>
                                            <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                                                <path d="M19.14,12.94c0.04-0.3,0.06-0.61,0.06-0.94c0-0.32-0.02-0.64-0.07-0.94l2.03-1.58c0.18-0.14,0.23-0.41,0.12-0.61 l-1.92-3.32c-0.12-0.22-0.37-0.29-0.59-0.22l-2.39,0.96c-0.5-0.38-1.03-0.7-1.62-0.94L14.4,2.81c-0.04-0.24-0.24-0.41-0.48-0.41 h-3.84c-0.24,0-0.43,0.17-0.47,0.41L9.25,5.35C8.66,5.59,8.12,5.92,7.63,6.29L5.24,5.33c-0.22-0.08-0.47,0-0.59,0.22L2.74,8.87 C2.62,9.08,2.66,9.34,2.86,9.48l2.03,1.58C4.84,11.36,4.82,11.69,4.82,12s0.02,0.64,0.07,0.94l-2.03,1.58 c-0.18,0.14-0.23,0.41-0.12,0.61l1.92,3.32c0.12,0.22,0.37,0.29,0.59,0.22l2.39-0.96c0.5,0.38,1.03,0.7,1.62,0.94l0.36,2.54 c0.05,0.24,0.24,0.41,0.48,0.41h3.84c0.24,0,0.44-0.17,0.47-0.41l0.36-2.54c0.59-0.24,1.13-0.56,1.62-0.94l2.39,0.96 c0.22,0.08,0.47,0,0.59-0.22l1.92-3.32c0.12-0.22,0.07-0.47-0.12-0.61L19.14,12.94z M12,15.6c-1.98,0-3.6-1.62-3.6-3.6 s1.62-3.6,3.6-3.6s3.6,1.62,3.6,3.6S13.98,15.6,12,15.6z"/>
                                            </svg>
                                            Settings
                                        </button>
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
                    </div>
                </div>
            </main>

            {/* Success Toast */}
            {showSuccessToast && (
                <div className="toast-container">
                    <div className="toast success">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
                        </svg>
                        Password changed successfully! Logging out...
                    </div>
                </div>
            )}

            {/* Settings Modal */}
            {showSettingsModal && (
                <div className="modal-overlay" onClick={() => setShowSettingsModal(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>Change Password</h2>
                            <button className="close-btn" onClick={() => setShowSettingsModal(false)}>&times;</button>
                        </div>
                        <form onSubmit={handlePasswordChange}>
                            <div className="form-group">
                                <label htmlFor="oldPassword">Current Password:</label>
                                <input
                                    type="password"
                                    id="oldPassword"
                                    name="oldPassword"
                                    value={passwordData.oldPassword}
                                    onChange={handlePasswordInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="newPassword">New Password:</label>
                                <input
                                    type="password"
                                    id="newPassword"
                                    name="newPassword"
                                    value={passwordData.newPassword}
                                    onChange={handlePasswordInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="confirmPassword">Confirm New Password:</label>
                                <input
                                    type="password"
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    value={passwordData.confirmPassword}
                                    onChange={handlePasswordInputChange}
                                    required
                                />
                            </div>
                            <div className="form-actions">
                                <button type="button" className="btn btn-cancel" onClick={() => setShowSettingsModal(false)}>
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-primary">
                                    Update Password
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