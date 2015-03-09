/**
 * Created by home on 03.03.15.
 */
function run() {
    var user;
    var appContainer = document.getElementsByClassName('chat')[0];
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);
}
function delegateEvent(evtObj) {
    if (evtObj.type === 'click' && evtObj.target.classList.contains('send')) {
        onSendMessageButton(evtObj);
    }
    if (evtObj.type === 'change' && evtObj.target.nodeName == 'INPUT') {
        var labelEl = evtObj.target.parentElement;
        setMarker(labelEl);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('delete')) {
        deleteMsg(evtObj);
    }
}
function setUsername() {
    user = "";
    user = prompt("Username","");
}
function changeUsername() {
    user = "";
    user = prompt("Username","");
}
function createItem(value) {
    var divItem = document.createElement('div');
    var textMessage = document.createElement('p');
    var checkbox = document.createElement('input');
    divItem.classList.add('messageStyle');
    textMessage.setAttribute('type', 'value');
    checkbox.setAttribute('type', 'checkbox');
    divItem.appendChild(textMessage);
    divItem.appendChild(checkbox);
    divItem.appendChild(document.createTextNode(user + ' :   ' + value ));
    return divItem;
}
function onSendMessageButton() {
    var text = document.getElementById('newMessage');
    add(text.value);
    text.value = '';
}
function add(value) {
    if(!value) {
        return;
    }
    var divItem = createItem(value);
    var messages = document.getElementsByClassName('b-field')[0];
    messages.appendChild(divItem);
}
function setMarker(labelEl) {
    if (labelEl.classList.contains('Check'))
    labelEl.classList.remove('Check');
    else labelEl.classList.add('Check');
}
function deleteMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length != 0) {
        for (var i = messages.length - 1; i >= 0; i--) {
            messages[i].remove();
        }
    }
}
