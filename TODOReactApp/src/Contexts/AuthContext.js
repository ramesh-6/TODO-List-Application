import React, { createContext, useState } from 'react';

export const authContext = createContext();

export const AuthProvider = ({ children }) => {
  const [username, setUsername] = useState();
  const [password, setPassword] = useState();
  const [confirmPassword, setConfirmPassword] = useState();

  return (
    <authContext.Provider value={{ username, setUsername, password, setPassword, confirmPassword, setConfirmPassword }}>
      {children}
    </authContext.Provider>
  );
};
