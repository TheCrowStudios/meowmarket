
function showUserDropdown() {
    const userDropdown = document.getElementById('user-dropdown');
    userDropdown?.classList.remove
}

function toggleNavbar() {
    const sideNav = document.getElementById('side-nav');
    const sideNavLinks = document.getElementById('side-nav-links');
    sideNav?.classList.toggle('w-16');
    sideNav?.classList.toggle('w-screen');
    sideNav?.classList.toggle('h-screen');
    sideNav?.classList.toggle('bg-primary')
    sideNavLinks?.classList.toggle('w-0');
    sideNavLinks?.classList.toggle('w-screen');
    sideNavLinks?.classList.toggle('flex');
    sideNavLinks?.classList.toggle('hidden');
}