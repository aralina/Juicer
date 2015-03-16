/**
 * Created by home on 03.03.15.
 */
var user = "";
var messageList = [];
function run() {
    var appContainer = document.getElementsByClassName('chat')[0];

    if (restore() != null) {
        var allMessages = restore();
        createAllMessages(allMessages);
    }
    if(user === "")
        alert("Welcome, stranger");
    else alert("Welcome back, " + user);
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
    if (evtObj.type === 'click' && evtObj.target.classList.contains('allMessages')) {
        selectMsg(evtObj);
    }
}

function setMarker(labelEl) {
    if (labelEl.classList.contains('Check'))
        labelEl.classList.remove('Check');
else labelEl.classList.add('Check');
}

function setUsername() {
    user = prompt("Username","");
}

function changeUsername() {
    user = prompt("Username","");
}

function onSendMessageButton() {
    if(user === "") {
        alert("Input username!");
        return;
    }

    var text = document.getElementById('sendMessage');
    var choice = document.getElementsByClassName('b-field')[0];
    var option = document.createElement("option");
    option.text = user + ' :   ' + text.value;
    option.value = Math.random() * (1000 - 1) + 1;
    var divItem = document.createElement('div');
    var textMessage = document.createElement('p');
    divItem.classList.add('messageStyle');
    divItem.id = option.value;
    textMessage.setAttribute('type', 'value');
    var checkbox = document.createElement('input');
    checkbox.setAttribute('type', 'checkbox');
    divItem.appendChild(textMessage);
    divItem.appendChild(checkbox);
    divItem.appendChild(document.createTextNode(option.text));

    choice.appendChild(divItem);

    messageList.push(theMessage(option.text, option.value));
    store(messageList);
    text.value = '';
}

var theMessage = function(text, value){
    return {
        name:user,
        message:text,
        check:true,
        id :value
    };
};

function selectMsg(evtObj) {
    var sendMessage = document.getElementById('sendMessage');
    var pos = document.getElementById("allMessages").selectedIndex;
    var choice = document.getElementById("allMessages")[pos];
    pos = choice.text.indexOf(':');
    if (choice.text != null) {
        sendMessage.value = choice.text.substring(pos + 4);
    }
}

function deleteMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length != 0) {
        for (var i = messages.length - 1; i >= 0; i--) {
            for (var j = 0; j < messageList.length; j++)
            if(messages[0].id == messageList[j].id) {
                var newMessageList = [];
                for (var k = 0; k < messageList.length - 1; k++)
                if (k != j)
                newMessageList[k] = messageList[k];
                messageList = newMessageList;
            }
            messages[i].remove();
        }
    }
    store(messageList);
}

function editMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length > 1) {
        alert("You chose too much messages! Please choose one");
        return;
    }
    var messageIn =  messages[0].innerHTML;
    var pos = messageIn.lastIndexOf(':') + 2;
    var messageOut = messageIn.substring(pos);
    var messageNew = prompt("Edit message:", messageOut);
    messageIn = messageIn.replace(messageOut, messageNew);
    messages[0].innerHTML = messageIn;
    setMarker(messages[0]);

}

function store(messageList) {

    var listAsString = JSON.stringify(messageList);
    localStorage.setItem("Messages list", listAsString);
}

function restore() {

    var item = localStorage.getItem("Messages list");
    return item && JSON.parse(item);
}

function addAllMessages(message) {
    var choice = document.getElementById("allMessages");
    var option = document.createElement("option");
    option.text =  message.message;
    option.value = message.id;
    var divItem = document.createElement('div');
    var textMessage = document.createElement('p');
    divItem.classList.add('messageStyle');
    textMessage.setAttribute('type', 'value');
    var checkbox = document.createElement('input');
    checkbox.setAttribute('type', 'checkbox');
    divItem.appendChild(textMessage);
    divItem.appendChild(checkbox);
    divItem.appendChild(document.createTextNode(option.text));
    choice.appendChild(divItem);
}

function createAllMessages(allMessages) {
    for (var i = 0; i < allMessages.length; i++) {
        messageList.push(allMessages[i]);
        addAllMessages(allMessages[i]);
    }
    user = allMessages[allMessages.length - 1].name;
}
