import React, { useState } from "react";
import logo from "../images/LC_Banja_Luka_red.png";
import { useNavigate } from "react-router-dom";
import "../index.css";
import axios from "axios";

const USER_REGEX = /^[a-zA-Z][a-zA-Z0-9-_]{3,20}$/;
const PWD_REGEX = /^[a-zA-Z0-9!@#$%^&*()_+.]{8,20}$/;

function delay(time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

export const Login = (props) => {
  const navigate = useNavigate();

  const [loginFailMessage, setLoginFailMessage] = useState(false);
  const [successfulRegistrationMessage, setShowSuccessfulRegistrationMessage] =
    useState(false);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [regUsername, setRegUsername] = useState("");
  const [regName, setRegName] = useState("");
  const [regSurname, setRegSurname] = useState("");
  const [regEmail, setRegEmail] = useState("");
  const [regPassword, setRegPassword] = useState("");

  const [isLogin, setIsLogin] = useState(true);


  const handleLoginSubmit = async (e) => {
    e.preventDefault();

    setLoginFailMessage(false);

    const loginForm = {
      username: username,
      lozinka: password,
    };

    try {
      const response = await axios.put(
        "http://localhost:8080/user/login",
        loginForm
      );
      if (response.status === 200) {
        console.log("Login successful");
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("userId", window.btoa(response.data.korisnik.idKorisnika))

        const loggedUser = response.data.korisnik;
        props.setLoggedUser(loggedUser);


        setTimeout(() => {
          navigate('/teams', { replace: true });
        }, 1000);
      }

      else {
        localStorage.clear();
        navigate('/', { replace: true });
      }

      props.setUserIsAuthenticated(true);
    } catch (error) {
      setLoginFailMessage(true);
    }
  };

  const handleRegistrationSubmit = async (e) => {
    e.preventDefault();

    const date = new Date();

    let currentDay = String(date.getDate()).padStart(2, "0");
    let currentMonth = String(date.getMonth() + 1).padStart(2, "0");
    let currentYear = date.getFullYear();
    let hours = date.getHours();
    if (hours < 10)
      hours = "0" + hours;
    let minutes = date.getHours();
    if (minutes < 10)
      minutes = "0" + minutes;
    let seconds = date.getHours();
    if (seconds < 10)
      seconds = "0" + seconds;
    let currentTime =
      hours + ":" + minutes + ":" + seconds;

    let currentDate = `${currentYear}-${currentMonth}-${currentDay}T${currentTime}`;

    try {
      const response = await axios.post("http://localhost:8080/question/add", {
        korisnickoIme: regUsername,
        ime: regName,
        lozinka: regPassword,
        prezime: regSurname,
        email: regEmail,
        datumKreiranja: currentDate,
      });

      if (response.status === 200) {
        console.log("Registration successful");

        setShowSuccessfulRegistrationMessage(true);
        await delay(1000);
        setIsLogin(true);
      }
    } catch (error) {
    }
  };

  return (
    <div className="login-form-container">
      <img src={logo} alt="" className="logo" />
      {isLogin ? (
        <div>
          <h2 className="heading">Prijava</h2>
          <form className="login-form" onSubmit={handleLoginSubmit}>
            <label htmlFor="username">Korisničko ime:</label>
            <input
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              type="username"
              placeholder="korisnicko.ime"
              pattern={USER_REGEX}
              id="username"
              name="username"
              required
            ></input>

            <label htmlFor="password">Lozinka:</label>
            <input
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              type="password"
              placeholder="********"
              pattern={PWD_REGEX}
              id="password"
              name="password"
              required
            ></input>
            {loginFailMessage ? (
              <p className="registration-info">Pogrešni pristupni podaci!</p>
            ) : (
              <></>
            )}

            <button type="submit" className="login-button">
              Prijavite se
            </button>
          </form>
          <u className="link" onClick={() => setIsLogin(false)}>
            Nemate nalog? Registrujte se!
          </u>
          <p className="registration-info">
            U slučaju zaboravljenih pristupnih podataka, javite se
            administratoru.
          </p>
        </div>
      ) : (
        <div>
          <h2 className="heading">Registracija</h2>
          <p className="registration-info">
            Nakon uspješne registracije, administrator mora da odobri nalog.
          </p>
          <form
            className="registration-form"
            onSubmit={handleRegistrationSubmit}
          >
            <div className="registration-attributes">
              <div className="registration-labels">
                <label className="registration-label" htmlFor="username">
                  Korisničko ime:
                </label>
                <label className="registration-label" htmlFor="name">
                  Ime:
                </label>
                <label className="registration-label" htmlFor="surname">
                  Prezime:
                </label>
                <label className="registration-label" htmlFor="email">
                  email:
                </label>
                <label className="registration-label" htmlFor="password">
                  Lozinka:
                </label>
              </div>

              <div className="registration-inputs">
                <input
                  value={regUsername}
                  onChange={(e) => setRegUsername(e.target.value)}
                  type="username"
                  name="username"
                  required
                ></input>
                <input
                  value={regName}
                  onChange={(e) => setRegName(e.target.value)}
                  type="text"
                  name="name"
                  required
                ></input>
                <input
                  value={regSurname}
                  onChange={(e) => setRegSurname(e.target.value)}
                  type="text"
                  pattern={USER_REGEX}
                  name="surname"
                  required
                ></input>
                <input
                  value={regEmail}
                  onChange={(e) => setRegEmail(e.target.value)}
                  type="text"
                  name="email"
                  required
                ></input>
                <input
                  value={regPassword}
                  onChange={(e) => setRegPassword(e.target.value)}
                  type="password"
                  name="password"
                  pattern={PWD_REGEX}
                  required
                ></input>
              </div>
            </div>

            {successfulRegistrationMessage ? (
              <p className="registration-info">Registracija uspješna!</p>
            ) : (
              <></>
            )}

            <button type="submit" className="login-button registration-button">
              Registrujte se
            </button>
          </form>
          <u className="link" onClick={() => setIsLogin(true)}>
            Već imate nalog? Prijavite se!
          </u>
        </div>
      )}
    </div>
  );
};
