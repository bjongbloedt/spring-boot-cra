import React, {useEffect} from 'react';
import logo from './logo.svg';
import './App.css';

function App() {

    // const [thing] = useState([]);

    useEffect(() => {
        load();
    }, []);


    const load = async () => {
        const response = await fetch("api/v1/widgets");
        console.log(response);
        const data = await response.json();

        console.log(data);
        // setThing(data)
    };


    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload.
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
