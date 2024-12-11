import React, { useState, useEffect } from 'react';
import axios from 'axios';

const App = () => {
  const [configured, setConfigured] = useState(false);
  const [systemRunning, setSystemRunning] = useState(false);
  const [totalTickets, setTotalTickets] = useState(0);
  const [releaseRate, setReleaseRate] = useState(0);
  const [retrievalRate, setRetrievalRate] = useState(0);
  const [maxCapacity, setMaxCapacity] = useState(0);
  const [updates, setUpdates] = useState([]);

  const BASE_URL = 'http://localhost:8080/api';

  const configureSystem = async () => {
    const payload = {
      totalTickets,
      ticketReleaseRate: releaseRate,
      customerRetrievalRate: retrievalRate,
      maxTicketCapacity: maxCapacity,
    };

    try {
      await axios.post(`${BASE_URL}/configure`, payload);
      setConfigured(true);
      alert('Configuration successful!');
    } catch (error) {
      console.error('Configuration failed:', error);
      alert('Configuration failed. Please try again.');
    }
  };

  const startSystem = async () => {
    try {
      await axios.post(`${BASE_URL}/start`);
      setSystemRunning(true);
    } catch (error) {
      console.error('Error starting system:', error);
      alert('Failed to start the system.');
    }
  };

  const stopSystem = async () => {
    try {
      await axios.post(`${BASE_URL}/stop`);
      setSystemRunning(false);
    } catch (error) {
      console.error('Error stopping system:', error);
      alert('Failed to stop the system.');
    }
  };

  useEffect(() => {
    if (systemRunning) {
      const interval = setInterval(async () => {
        try {
          const response = await axios.get(`${BASE_URL}/tickets`);
          setUpdates((prevUpdates) => [...prevUpdates, response.data]);
        } catch (error) {
          console.error('Error fetching updates:', error);
          setSystemRunning(false);
          clearInterval(interval);
        }
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [systemRunning]);

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-4xl mx-auto bg-white shadow-md rounded p-6">
        {!configured ? (
          <div>
            <h1 className="text-xl font-bold mb-4">Configure the System</h1>
            <form
              onSubmit={(e) => {
                e.preventDefault();
                configureSystem();
              }}
            >
              <div className="mb-4">
                <label className="block text-sm font-medium mb-1">Total Tickets</label>
                <input
                  type="number"
                  value={totalTickets}
                  onChange={(e) => setTotalTickets(Number(e.target.value))}
                  className="border p-2 w-full rounded"
                />
              </div>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-1">Ticket Release Rate (ms)</label>
                <input
                  type="number"
                  value={releaseRate}
                  onChange={(e) => setReleaseRate(Number(e.target.value))}
                  className="border p-2 w-full rounded"
                />
              </div>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-1">Customer Retrieval Rate (ms)</label>
                <input
                  type="number"
                  value={retrievalRate}
                  onChange={(e) => setRetrievalRate(Number(e.target.value))}
                  className="border p-2 w-full rounded"
                />
              </div>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-1">Maximum Ticket Capacity</label>
                <input
                  type="number"
                  value={maxCapacity}
                  onChange={(e) => setMaxCapacity(Number(e.target.value))}
                  className="border p-2 w-full rounded"
                />
              </div>
              <button type="submit" className="bg-blue-500 text-white py-2 px-4 rounded">
                Configure
              </button>
            </form>
          </div>
        ) : (
          <div>
            <h1 className="text-xl font-bold mb-4">System Controls</h1>
            <button
              onClick={startSystem}
              className="bg-green-500 text-white py-2 px-4 rounded mr-2"
              disabled={systemRunning}
            >
              Start System
            </button>
            <button
              onClick={stopSystem}
              className="bg-red-500 text-white py-2 px-4 rounded"
              disabled={!systemRunning}
            >
              Stop System
            </button>
            <h2 className="text-lg font-bold mt-6">Real-time Updates</h2>
            <div className="mt-4 bg-gray-50 border p-4 rounded max-h-60 overflow-y-auto">
              {updates.map((update, index) => (
                <p key={index}>{JSON.stringify(update)}</p>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default App;
