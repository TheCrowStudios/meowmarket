
function showUserDropdown() {
    const userDropdown = document.getElementById('user-dropdown');
    userDropdown?.classList.remove
}

function toggleNavbar() {
    const sideNav = document.getElementById('side-nav');
    const sideNavLinks = document.getElementById('side-nav-links');
    sideNavLinks?.classList.toggle('-translate-x-full');
    sideNavLinks?.classList.toggle('opacity-0');
}