import React, {useEffect, useState} from 'react';
import logo from './logo.svg';
import './App.css';

function App() {

    const [name, setName] = useState("Unknown User");

    useEffect(() => {
        load();
    }, []);


    const load = async () => {
        const response = await fetch("http://localhost:8080/api/v1/widgets");
        const data = await response.json();
        if (data[0].name) {
            setName(data[0].name)
        }
    };


    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to {name}
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
