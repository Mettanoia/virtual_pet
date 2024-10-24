document.addEventListener('DOMContentLoaded', () => {

    const petElement = document.getElementById('pet');
    const interactButton = document.getElementById('interact-button');
    const interactionMenu = document.getElementById('interaction-menu');
    const selectionMenu = document.getElementById('selection-menu');
    const selectionList = document.getElementById('selection-list');
    const selectionTitle = document.getElementById('selection-title');

    // Make the pet bounce to imitate steps
    petElement.style.animation = 'move 5s infinite alternate, bounce 0.5s infinite';

    // Add bounce keyframes
    const styleSheet = document.styleSheets[0];
    styleSheet.insertRule(`
        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-10px); }
        }
    `, styleSheet.cssRules.length);

    // Pet click event to show interaction menu
    petElement.addEventListener('click', () => {
        interactionMenu.style.display = 'block';
    });

    // Interact button event
    interactButton.addEventListener('click', () => {
        interactionMenu.style.display = 'block';
    });

    // Feed button event
    document.getElementById('feed-button').addEventListener('click', () => {
        interactionMenu.style.display = 'none';
        selectionTitle.textContent = 'Choose Food';
        loadItems('food');
    });

    // Gift button event
    document.getElementById('gift-button').addEventListener('click', () => {
        interactionMenu.style.display = 'none';
        selectionTitle.textContent = 'Choose Gift';
        loadItems('gift');
    });

    function loadItems(type) {
        // Fetch items from server or define them here
        const items = type === 'food' ? ['Apple', 'Steak', 'Bread'] : ['Ball', 'Toy', 'Bone'];

        selectionList.innerHTML = '';
        items.forEach(item => {
            const li = document.createElement('li');
            li.textContent = item;
            li.addEventListener('click', () => selectItem(type, item));
            selectionList.appendChild(li);
        });
        selectionMenu.style.display = 'block';
    }

    function selectItem(type, item) {
        // Send POST request to /pet endpoint
        fetch('/pet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ type, item, petId: /* pet ID here */ })
        })
            .then(response => response.json())
            .then(data => {
                // Handle response, update pet state if necessary
                selectionMenu.style.display = 'none';
                alert(`${type} given: ${item}`);
            })
            .catch(error => console.error('Error:', error));
    }
});
