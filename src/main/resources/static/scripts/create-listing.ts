let previewVisible = false;
const textarea = document.getElementById('longDescription') as HTMLTextAreaElement;
const preview = document.getElementById('preview') as HTMLDivElement;

function togglePreview() {
    previewVisible = !previewVisible;
    preview?.classList.toggle('hidden');
    if (previewVisible) updatePreview();
}

function updatePreview() {
    const content = textarea.value;
    const sanitizedContent = new DOMParser().parseFromString(content, 'text/html').body.innerHTML;
    if (preview) preview.innerHTML = sanitizedContent;
}