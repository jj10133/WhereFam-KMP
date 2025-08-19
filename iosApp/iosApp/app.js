const { IPC } = BareKit;

IPC.setEncoding('utf8');

// Send 3 messages at 2-second intervals
setTimeout(() => {
    IPC.write('Message 1\n');
}, 2000);

setTimeout(() => {
    IPC.write('Message 2\n');
}, 4000);

setTimeout(() => {
    IPC.write('Message 3\n');
}, 6000);

// Optional: 4th message after 8 seconds
setTimeout(() => {
    IPC.write('Message 4\n');
}, 8000);
