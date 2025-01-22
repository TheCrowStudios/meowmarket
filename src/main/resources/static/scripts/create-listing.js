"use strict";
let previewVisible = false;
const textarea = document.getElementById('longDescription');
const preview = document.getElementById('preview');
function togglePreview() {
    previewVisible = !previewVisible;
    preview === null || preview === void 0 ? void 0 : preview.classList.toggle('hidden');
    if (previewVisible)
        updatePreview();
}
function updatePreview() {
    const content = textarea.value;
    const sanitizedContent = new DOMParser().parseFromString(content, 'text/html').body.innerHTML;
    if (preview)
        preview.innerHTML = sanitizedContent;
}
