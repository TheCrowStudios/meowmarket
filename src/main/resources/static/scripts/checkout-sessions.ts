async function createCheckoutSessionListing(id: number) {
    let quantity = "1";
    const inpId = 'quantity-' + id.toString();
    const inpQuantity = document.getElementById(inpId) as HTMLInputElement;

    if (!isNaN(Number.parseInt(inpQuantity.value))) quantity = inpQuantity.value;

    try {
        const response = await fetch('/api/stripe/checkout/listing', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                listingId: id,
                quantity: quantity
            })
        });

        if (response.ok) {
            const html = await response.text();
            document.write(html);
        } else {
            showMessage('Could not create checkout session :c')
        }
    } catch {
        showMessage('Could not create checkout session :c')
    }
}

function verifyQuantity(id: number) {
    const inpQuantity = document.getElementById('quantity-' + id) as HTMLInputElement;

    if (isNaN(Number.parseInt(inpQuantity.value))) {
        inpQuantity.value = "1";
        return;
    }

    if (Number.parseInt(inpQuantity.value) > Number.parseInt(inpQuantity.max)) inpQuantity.value = inpQuantity.max
}

function verifyAndUpdateCartQuantity(id: number) {
    verifyQuantity(id);
    const inpQuantity = document.getElementById('quantity-' + id) as HTMLInputElement;
    updateQuantity(id, inpQuantity.valueAsNumber);
}