import React, { createContext, useState } from 'react';

export const authContext = createContext();

export const AuthProvider = ({ children }) => {
  const [username, setUsername] = useState();
  const [password, setPassword] = useState();
  const [confirmPassword, setConfirmPassword] = useState();
  const [email, setEmail] = useState();

  return (
    <authContext.Provider value={{ username, setUsername, password, setPassword, confirmPassword, setConfirmPassword, email, setEmail}}>
      {children}
    </authContext.Provider>
  );
};
