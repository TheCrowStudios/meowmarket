"use strict";
const submit = document.getElementById('submit');
const inpPassword = document.getElementById('password');
const inpConfirmPassword = document.getElementById('confirm-password');
const error = document.getElementById('error');
submit.addEventListener('click', (event) => {
    if (!verifyLogin()) {
        event.preventDefault();
        error.classList.toggle('opacity-0');
        error.innerText = 'Passwords do not match';
    }
});
function verifyLogin() {
    if (!verifyPassword()) {
        return false;
    }
    return true;
}
function verifyPassword() {
    if (inpPassword.value != inpConfirmPassword.value) {
        return false;
    }
    return true;
}
