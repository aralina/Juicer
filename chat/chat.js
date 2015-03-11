/**
 * Created by home on 03.03.15.
 */
var user = "";
function run() {

    var appContainer = document.getElementsByClassName('chat')[0];
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);
}
function delegateEvent(evtObj) {
    if (evtObj.type === 'click' && evtObj.target.classList.contains('send')) {
        onSendMessageButton(evtObj);
    }
    if (evtObj.type === 'change' && evtObj.target.nodeName == 'INPUT'
        && evtObj.target.classList.contains('send') == false
        && evtObj.target.classList.contains('edit') == false
        && evtObj.target.classList.contains('delete') == false
        && evtObj.target.classList.contains('b-input') == false) {
        var labelEl = evtObj.target.parentElement;
        setMarker(labelEl);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('delete')) {
        deleteMsg(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('edit')) {
        editMsg(evtObj);
    }
}
function setUsername() {
    user = prompt("Username","");
}
function changeUsername() {
    user = prompt("Username","");
}
function createItem(value) {
    if (user === "") {
        alert("Input your username!");
        return;
    }
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
function editMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length > 1) {
        alert("You chose too much messages! Please choose one");
        return;
    }
    var messageIn =  messages[0].innerHTML;
    var pos = messageIn.indexOf(':') + 4;
    var messageOut = messageIn.substring(pos);
    var messageNew = prompt("Edit message:", messageOut);
    messageIn = messageIn.replace(messageOut, messageNew);
    messages[0].innerHTML = messageIn;
    setMarker(messages[0]);
}