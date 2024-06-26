import React, { useState, useEffect } from "react";
import { Table } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.css";
import axios from "axios";

const UserList = ({ switchTab, selectUser, selectTeam, setTeams }) => {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");
  const [teams, setTimove] = useState([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {

      const response = await axios.get("http://localhost:8080/user/getAll", {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });

      if (response.status === 403) {
        localStorage.clear();
        switchTab("login");
      }

      let counter = 1;
      setUsers((response.data).map(user => ({...user, idTable: counter++})));

      const responseTeams = await axios.get(
        "http://localhost:8080/team/getAll",
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + localStorage.getItem("token"),
          },
        }
      );

      if (responseTeams.status === 403) {
        localStorage.clear();
        switchTab("login");
      }

      setTimove(responseTeams.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  // const users = [
  //     {
  //         id: 1,
  //         korisnickoIme: "ki1",
  //         ime: "ime1",
  //         prezime: "prezime1",
  //         email: "email",
  //         uloga: "koordinator"
  //     },
  //     {
  //         id: 2,
  //         korisnickoIme: "ki2",
  //         ime: "ime2",
  //         prezime: "prezime2",
  //         email: "email",
  //         uloga: "koordinator"
  //     },
  // ];

  const handleUserClick = (user) => {
    selectTeam(teams.find((t) => t.idKoordinator === user.idKorisnika));
    selectUser(user);
    setTeams(teams);
    switchTab("user");
  };

  return (
    <div className="user-list">
      <div className="search">
        <input
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Pretraga"
        ></input>
      </div>

      <h2>Korisnici</h2>
      <div className=" table-wrapper table-wrapper-users">
        <Table hover className="table table-bordered my-table">
          <thead>
            <tr>
              <th className="table-header" scope="row">#</th>
              <th className="table-header">Korisničko ime</th>
              <th className="table-header">Ime</th>
              <th className="table-header">Prezime</th>
              {<th className="table-header">Uloga</th>}
            </tr>
          </thead>
          <tbody>
            {users
              .filter((korisnik) => {
                return search.toLowerCase() === ""
                  ? korisnik
                  : korisnik.korisnickoIme.toLowerCase().includes(search) ||
                  korisnik.ime.toLowerCase().includes(search) ||
                  korisnik.prezime.toLowerCase().includes(search) ||
                  korisnik.uloga.toLowerCase().includes(search);
              })
              .map((korisnik) => (
                <tr
                  key={korisnik.idKorisnika}
                  className="table-row"
                  onClick={() => handleUserClick(korisnik)}
                >
                  <th>{korisnik.idTable}</th>
                  <td>{korisnik.korisnickoIme}</td>
                  <td>{korisnik.ime}</td>
                  <td>{korisnik.prezime}</td>
                  {<td>{korisnik.uloga}</td>}
                </tr>
              ))}
          </tbody>
        </Table>
      </div>
    </div>
  );
};

export default UserList;
