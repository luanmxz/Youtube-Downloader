function clearUrlInput() {
	document.getElementById('url').value = '';
	document.getElementById('clear-input').classList.add('hidden');
}

function showSpinner() {
	document.getElementById('button-text').classList.add('hidden');
	document.getElementById('button-spinner').classList.remove('hidden');
	document.getElementById('convert-button').disabled = true;
}

function hideSpinner() {
	document.getElementById('button-text').classList.remove('hidden');
	document.getElementById('button-spinner').classList.add('hidden');
	document.getElementById('convert-button').disabled = false;
}

function showToast(message, error) {
	const toast = document.getElementById('toast');
	const messageElement = document.getElementById('toastMessage');
	messageElement.textContent = message;

	toast.classList.remove('translate-y-full', 'opacity-0');
	toast.classList.add('translate-y-0', 'opacity-100');

	if (error) {
		toast.classList.add('bg-red-500');
	} else {
		toast.classList.add('bg-green-500');
	}

	setTimeout(() => {
		toast.classList.add('translate-y-full', 'opacity-0');
		toast.classList.remove('translate-y-0', 'opacity-100');
	}, 3000);
}

function validateForm(event) {
	if (document.getElementById('url').value.length < 1) {
		showToast(
			'Você deve inserir a URL de algum vídeo antes de converter!',
			true
		);
		event.preventDefault();
		return false;
	}

	var qualityInput = document.getElementById('quality');
	if (qualityInput.value !== '128k') {
		qualityInput.value = '128k';
	}

	showSpinner();

	return true;
}

function copyUrlValue(url) {
	navigator.clipboard.writeText(url);
	showToast('Copied to clipboard!', false);
}

document.body.addEventListener('htmx:afterSwap', function (event) {
	if (event.detail.target.id === 'result') {
		clearUrlInput();
		setTimeout(hideSpinner, 100);

		var lastAddedElement = event.detail.target.lastElementChild;
		if (
			lastAddedElement &&
			lastAddedElement !== null &&
			lastAddedElement !== undefined
		) {
			var index = lastAddedElement.id;
			var fileName =
				lastAddedElement.querySelector('#videoName').textContent ||
				lastAddedElement.querySelector('#videoName').innerText ||
				lastAddedElement.querySelector('#videoName').value;
			var url = lastAddedElement.querySelector('#hidden_url').value;

			localStorage.setItem(
				index,
				JSON.stringify({ url: url, fileName: fileName })
			);
		}
	}
});

document.getElementById('url').addEventListener('input', function () {
	var clearButton = document.getElementById('clear-input');
	if (this.value) {
		clearButton.classList.remove('hidden');
	} else {
		clearButton.classList.add('hidden');
	}
});

document.getElementById('clear-input').addEventListener('click', clearUrlInput);

function removeItem(itemId) {
	localStorage.removeItem(itemId);
	console.log(itemId);
	document.getElementById(itemId).remove();
}

function toggleDarkMode() {
	document.documentElement.classList.toggle('dark');
	const sunIcon = document.getElementById('sun-icon');
	const moonIcon = document.getElementById('moon-icon');
	sunIcon.classList.toggle('hidden');
	moonIcon.classList.toggle('hidden');
}

document
	.getElementById('darkModeToggle')
	.addEventListener('change', toggleDarkMode);

if (localStorage.getItem('darkMode') === 'enabled') {
	document.getElementById('darkModeToggle').checked = true;
	toggleDarkMode();
}

document
	.getElementById('darkModeToggle')
	.addEventListener('change', function () {
		if (this.checked) {
			localStorage.setItem('darkMode', 'enabled');
		} else {
			localStorage.setItem('darkMode', null);
		}
	});
