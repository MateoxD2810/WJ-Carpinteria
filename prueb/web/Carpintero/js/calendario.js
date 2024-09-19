let currentDate = new Date();

document.addEventListener('DOMContentLoaded', () => {
    renderCalendar(currentDate);
});

function renderCalendar(date) {
    const calendar = document.querySelector('.calendar');
    const daysContainer = document.getElementById('days');
    const monthYearText = document.getElementById('month');

    const firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
    const lastDayOfMonth = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    const daysInMonth = lastDayOfMonth.getDate();
    const startDay = firstDayOfMonth.getDay();
    const endDay = lastDayOfMonth.getDay();

    monthYearText.textContent = getMonthName(date.getMonth()) + ' ' + date.getFullYear();

    let daysHTML = '';

    for (let i = 1 - startDay; i <= daysInMonth + (6 - endDay); i++) {
        const day = new Date(date.getFullYear(), date.getMonth(), i);
        daysHTML += `<div class="day ${isSameMonth(day, date) ? '' : 'other-month'}" data-day="${i > 0 && i <= daysInMonth ? i : ''}" onclick="selectDay(event)">${i > 0 && i <= daysInMonth ? i : ''}</div>`;
    }

    daysContainer.innerHTML = daysHTML;
}

function previousMonth() {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar(currentDate);
    clearSelectedDay();
    clearNote();
}

function nextMonth() {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar(currentDate);
    clearSelectedDay();
    clearNote();
}

function selectDay(event) {
    const selectedDay = event.target.dataset.day;
    if (selectedDay) {
        clearSelectedDay();
        event.target.classList.add('selected');
        document.getElementById('selectedDate').textContent = selectedDay;
        loadNote();
    }
}

function saveNote() {
    const note = document.getElementById('note').value;
    const selectedDay = document.querySelector('.day.selected').dataset.day;
    const storageKey = `note_${currentDate.getMonth()}_${currentDate.getFullYear()}_${selectedDay}`;
    localStorage.setItem(storageKey, note);
    const dateTime = new Date().toLocaleString();
    alert(`Nota guardada correctamente.\nFecha y hora: ${dateTime}`);
}

function loadNote() {
    const selectedDay = document.querySelector('.day.selected').dataset.day;
    const storageKey = `note_${currentDate.getMonth()}_${currentDate.getFullYear()}_${selectedDay}`;
    const note = localStorage.getItem(storageKey);
    if (note) {
        document.getElementById('note').value = note;
    } else {
        clearNote();
    }
}

function clearSelectedDay() {
    const selected = document.querySelector('.day.selected');
    if (selected) {
        selected.classList.remove('selected');
    }
    document.getElementById('selectedDate').textContent = '-';
}

function clearNote() {
    document.getElementById('note').value = '';
}

function isSameMonth(day, month) {
    return day.getMonth() === month.getMonth();
}

function getMonthName(month) {
    const monthNames = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    return monthNames[month];
}
