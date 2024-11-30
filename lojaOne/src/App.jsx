// src/App.jsx

import React from 'react';
import { AuthProvider } from './service/AuthContext'; // Importe o AuthProvider
import Routessistem from "./routes";

export default function App() {
  return (
    <AuthProvider>
        <Routessistem />
    </AuthProvider>
  );
}