export function api() {
    const URL = "http://localhost:8080/devices/0/rechable-devices";
    const evtSource = new EventSource(URL);

    evtSource.onmessage = (event) => {
        if (event.data) {
            const raw = JSON.parse(event.data);

            switch (raw.type) {
                case "NETWORK_SNAPSHOT":
                // handleSnapshot(snapshot);
                break;

            case "DEVICE_STATE_CHANGED":
                // handleDeviceStateChange(changeEvent);
                break;
            }
        };
    }
    evtSource.onerror = (err) => {
        console.error("EventSource failed:", err);
    };
    evtSource.close();
}