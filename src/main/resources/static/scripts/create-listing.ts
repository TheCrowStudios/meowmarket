let previewVisible = false;
const textarea = document.getElementById('longDescription') as HTMLTextAreaElement;
const preview = document.getElementById('preview') as HTMLDivElement;

function togglePreview() {
    previewVisible = !previewVisible;
    preview.classList.toggle('hidden');
    textarea.classList.toggle('hidden');
    if (previewVisible) updatePreview();
}

function updatePreview() {
    const content = textarea.value;
    const sanitizedContent = new DOMParser().parseFromString(content, 'text/html').body.innerHTML;
    if (preview) preview.innerHTML = sanitizedContent;
}

class ImageOrderManager {
    private fileInput: HTMLInputElement;
    private imageGallery: HTMLDivElement;
    private orderedFiles: File[] = [];
    private form: HTMLFormElement;

    constructor() {
        this.fileInput = document.getElementById('image-input') as HTMLInputElement;
        // this.dropZone = document.getElementById('image-drop-zone') as HTMLDivElement;
        this.imageGallery = document.getElementById('image-gallery') as HTMLDivElement;
        this.form = document.querySelector('form') as HTMLFormElement;

        this.dragStart = this.dragStart.bind(this);
        this.dragOver = this.dragOver.bind(this);
        this.drop = this.drop.bind(this);
        this.handleFormSubmission = this.handleFormSubmission.bind(this);

        this.setUpEventListeners();
    }

    private setUpEventListeners(): void {
        this.fileInput.addEventListener('change', (event) => {
            const files = (event.target as HTMLInputElement).files;
            if (files) this.processFiles(files);
        });
        
        this.form.addEventListener('submit', (event) => this.handleFormSubmission(event));
    }

    private processFiles(files: FileList): void {
        const newFiles = Array.from(files).filter(file => file.type.startsWith('image/')).slice(0, 10);

        this.orderedFiles = newFiles;
        this.renderImageGallery();
    }

    private renderImageGallery(): void {
        this.imageGallery.innerHTML = '';

        this.orderedFiles.forEach((file, index) => {
            const wrapper = this.createImageWrapper(file, index);
            this.imageGallery.appendChild(wrapper);
        })
    }

    private createImageWrapper(file: File, index: number): HTMLDivElement {
        const imageWrapper = document.createElement('div') as HTMLDivElement;
        imageWrapper.classList.add(
            'relative', 'group', 'cursor-move',
            'border', 'border-gray-200', 'rounded'
        );
        imageWrapper.draggable = true;
        console.log('image wrapper index: ', index.toString());
        imageWrapper.dataset.index = index.toString();

        const image = document.createElement('img') as HTMLImageElement;
        image.classList.add('w-full', 'h-auto', 'object-cover', 'rounded');
        image.dataset.index = index.toString();

        const reader = new FileReader();
        reader.onload = (e) => {
            image.src = e.target?.result as string;
        };
        reader.readAsDataURL(file);

        const removeButton = document.createElement('button') as HTMLButtonElement;
        removeButton.innerHTML = 'remove';
        removeButton.classList.add('absolute', 'top-1', 'right-1', 'bg-red-500',
            'text-white', 'rounded-full', 'w-6', 'h-6',
            'flex', 'items-center', 'justify-center',
            'opacity-0', 'group-hover:opacity-100',
            'transition', 'duration-300');

        removeButton.addEventListener('click', () => {
            this.removeImage(index);
        });

        imageWrapper.addEventListener('dragstart', (event) => this.dragStart(event));
        imageWrapper.addEventListener('dragover', this.dragOver);
        imageWrapper.addEventListener('drop', this.drop);
        imageWrapper.appendChild(image);
        imageWrapper.appendChild(removeButton);

        return imageWrapper;
    };

    private removeImage(index: number): void {
        this.orderedFiles.splice(index, 1);
        this.renderImageGallery();
    }

    private dragStart(event: DragEvent) {
        const targetDiv = event.target instanceof HTMLImageElement ? event.target.closest('[data-index]') as HTMLImageElement : null
        if (event.dataTransfer && targetDiv) {
            const index = targetDiv.dataset.index;
            console.log('dragStart index: ' + index);
            event.dataTransfer.setData('text/plain', index || '');
        }

        if (event.dataTransfer) event.dataTransfer.effectAllowed = 'move';
    }

    private dragOver(event: DragEvent) {
        event.preventDefault();

        if (event.dataTransfer) {
            event.dataTransfer.dropEffect = 'move';
        }
    }

    private drop(event: DragEvent) {
        event.preventDefault();

        if (!event.dataTransfer) return;

        const draggedIndex = parseInt(event.dataTransfer.getData('text/plain'), 10);
        const targetElement = event.target as HTMLImageElement;
        const dropTargetIndex = parseInt(targetElement.closest('img')?.dataset.index || '-1', 10);
        console.log('dragged index: ' + draggedIndex.toString());
        console.log('drop index: ' + dropTargetIndex.toString());

        if (draggedIndex != dropTargetIndex && dropTargetIndex != -1) {
            const [movedFile] = this.orderedFiles.splice(draggedIndex, 1);
            this.orderedFiles.splice(dropTargetIndex, 0, movedFile);

            this.renderImageGallery();
        }
    }

    private handleFormSubmission(event: Event): void {
        event.preventDefault();

        const dataTransfer = new DataTransfer();
        this.orderedFiles.forEach(file => dataTransfer.items.add(file));

        this.fileInput.files = dataTransfer.files;

        this.form.submit();
    }
};

document.addEventListener('DOMContentLoaded', () => {
    try {
        new ImageOrderManager();
    } catch (error) {
        console.error('Could not initialize image order manager: ' + error);
    }
});