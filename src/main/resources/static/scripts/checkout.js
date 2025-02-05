// This is your test secret API key.
const stripe = Stripe("pk_test_51QjVLQDHTVEuSG0o14Ajryf74SuMKI2BvGN8Rb7gQyVA0VnlN6eqbliZbMvfmREarRMgzu6qMRWyoQRyhyEXXbIj00RmrjTFdD");
const path = document.getElementById('path').innerText;
const listingId = document.getElementById('listingId').innerText;
const quantity = document.getElementById('quantity').innerText;

initialize();

// Create a Checkout Session
async function initialize() {
  const fetchClientSecret = async () => {
    const response = await fetch(path, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        listingId: listingId,
        quantity: quantity
      })
    });
    const { clientSecret } = await response.json();
    return clientSecret;
  };

  const checkout = await stripe.initEmbeddedCheckout({
    fetchClientSecret,
  });

  // Mount Checkout
  checkout.mount('#checkout');
}