"use strict";
const submit = document.getElementById('submit');
const inpUsername = document.getElementById('username');
const inpEmail = document.getElementById('email');
const inpPassword = document.getElementById('password');
const inpConfirmPassword = document.getElementById('confirm-password');
const usernamePattern = /^[a-zA-Z0-9_-]{3,20}$/;
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const error = document.getElementById('error');
submit.addEventListener('click', (event) => {
    if (!validateRegister()) {
        event.preventDefault();
        error.classList.toggle('opacity-0');
    }
});
function validateRegister() {
    if (!validateUsername() || !validateEmail() || !verifyPassword()) {
        return false;
    }
    return true;
}
function validateUsername() {
    const errorUsername = document.getElementById('error-username');
    if (inpUsername.value.trim() === '') {
        errorUsername.classList.remove('hidden');
        errorUsername.textContent = 'A username is required';
        return false;
    }
    else if (!usernamePattern.test(inpUsername.value)) {
        errorUsername.classList.remove('hidden');
        errorUsername.textContent = 'Username must be between 3 and 20 characters long cannot contain special characters';
    }
    else {
        errorUsername.classList.add('hidden');
        errorUsername.textContent = '';
        return true;
    }
}
function validateEmail() {
    const errorEmail = document.getElementById('error-email');
    if (inpEmail.value.trim() === '') {
        errorEmail.classList.remove('hidden');
        errorEmail.textContent = 'An email address is required';
        return false;
    }
    else if (!emailPattern.test(inpEmail.value)) {
        errorEmail.classList.remove('hidden');
        errorEmail.textContent = 'Please enter a valid email address';
        return false;
    }
    else {
        errorEmail.classList.add('hidden');
        errorEmail.textContent = '';
        return true;
    }
}
function validatePassword() {
    const errorPassword = document.getElementById('error-password');
    if (inpPassword.value.trim() === '') {
        errorPassword.classList.remove('hidden');
        errorPassword.textContent = 'A password is required';
        return false;
    }
    if (inpPassword.value != inpConfirmPassword.value) {
        errorPassword.classList.remove('hidden');
        errorPassword.textContent = 'Passwords do not match';
        return false;
    }
    else {
        errorPassword.classList.add('hidden');
        errorPassword.textContent = '';
        return true;
    }
    return true;
}
