"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
function createCheckoutSessionListing(id) {
    return __awaiter(this, void 0, void 0, function* () {
        let quantity = "1";
        const inpId = 'quantity-' + id.toString();
        const inpQuantity = document.getElementById(inpId);
        if (!isNaN(Number.parseInt(inpQuantity.value)))
            quantity = inpQuantity.value;
        try {
            const response = yield fetch('/api/stripe/checkout/listing', {
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
                const html = yield response.text();
                document.write(html);
            }
            else {
                showMessage('Could not create checkout session :c');
            }
        }
        catch (_a) {
            showMessage('Could not create checkout session :c');
        }
    });
}
function verifyQuantity(id) {
    const inpQuantity = document.getElementById('quantity-' + id);
    if (isNaN(Number.parseInt(inpQuantity.value))) {
        inpQuantity.value = "1";
        return;
    }
    if (Number.parseInt(inpQuantity.value) > Number.parseInt(inpQuantity.max))
        inpQuantity.value = inpQuantity.max;
}
function verifyAndUpdateCartQuantity(id) {
    verifyQuantity(id);
    const inpQuantity = document.getElementById('quantity-' + id);
    updateQuantity(id, inpQuantity.valueAsNumber);
}
