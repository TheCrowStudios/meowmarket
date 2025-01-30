const submit = document.getElementById('submit') as HTMLButtonElement;
const inpPassword = document.getElementById('password') as HTMLInputElement;
const inpConfirmPassword = document.getElementById('confirm-password') as HTMLInputElement;
const error = document.getElementById('error') as HTMLSpanElement;

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