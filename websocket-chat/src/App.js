import { useEffect, useState } from "react";
import axios from "axios";
import { v4 } from "uuid";
import { Client } from '@stomp/stompjs';
import SockJS from "sockjs-client";
import "./App.css";

function App() {
  const [sender, setSender] = useState();
  const [receiver, setReceiver] = useState();
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState();
  const [stompClient, setStompClient] = useState(null);

  useEffect(() => {
    const connectWebSocket = () => {
      const socket = new SockJS("http://localhost:8080/websocket-endpoint");
  
      const stompClient = new Client({
        webSocketFactory: () => socket, // Use the SockJS socket as the WebSocket factory
        connectHeaders: {},
        debug: function (str) {
          console.log(str);
        },
        onConnect: (frame) => {
          console.log("WebSocket connected!");
          stompClient.subscribe("/topic/messages", (message) => {
            const newMessage = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, newMessage]);
          });
        },
        onStompError: (frame) => {
          console.error("Broker reported error: ", frame.headers.message);
          console.error("Additional details: ", frame.body);
        },
        onDisconnect: () => {
          console.log("Disconnected from WebSocket");
        },
      });
  
      stompClient.activate();
  
      return stompClient;
    };
  
    setStompClient(connectWebSocket());
  
    return () => {
      if (stompClient) {
        stompClient.deactivate(); // Properly deactivate the client
      }
    };
  }, []);
  

  const sendMessage = () => {
    if (stompClient && stompClient.connected) { // Check if the stompClient is connected
      const smessage = { sender, receiver, message };
      stompClient.publish({ destination: "/app/chat", body: JSON.stringify(smessage) });
    } else {
      console.error("Stomp client is not connected.");
    }
  };
  

  const getData = () => {
    axios
      .get("http://localhost:8080/v1/chat")
      .then((response) => {
        setMessages(response.data);
        console.log(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  useEffect(() => {
    getData();
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    setMessage("");
    sendMessage();
    axios
      .post("http://localhost:8080/v1/chat", { sender, receiver, message })
      .then(() => {
        setTimeout(() => {
          getData();
        }, 100);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <>
      <div className="select-box">
        <p
          className="select"
          onClick={() => {
            setSender("user1");
            setReceiver("user2");
          }}
        >
          user1
        </p>
        <p
          className="select"
          onClick={() => {
            setSender("user2");
            setReceiver("user1");
          }}
        >
          user2
        </p>
      </div>
      <div className="container">
        <div className="box">
          <div className="header">
            <p>{sender}</p>
          </div>
          <div className="main">
            <ul>
              {messages?.map((data) => {
                let content;

                if (data.sender == sender) {
                  content = (
                    <li className={"currentUser"} key={v4()}>
                      <p>{data.message}</p>
                    </li>
                  );
                } else {
                  content = (
                    <li className={"targetUser"} key={v4()}>
                      <p>{data.message}</p>
                    </li>
                  );
                }

                return content;
              })}
            </ul>
          </div>
          <div className="chatbox">
            <form onSubmit={submit}>
              <input
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              />
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default App;