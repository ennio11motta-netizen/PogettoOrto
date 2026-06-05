import { useEffect, useState } from "react";
import "./App.css";
import SpeciesList from "./components/species/SpeciesList";
import CreateSpeciesForm from "./components/species/CreateSpeciesForm";
import CreateGardenForm from "./components/garden/CreateGardenForm";
import GardenPlants from "./components/garden/GardenPlants";
import AddPlantForm from "./components/garden/AddPlantForm";
import SimulationStart from "./components/simulation/SimulationStart";
import SimulationResults from "./components/simulation/SimulationResults";

import {
  getSpecies,
  createSpecies,
  deleteSpecies,
  createGarden,
  getGardens,
  addPlant,
  deletePlant,
  getPlantsByLocation,
  runGardenSimulation,
  deleteGardenSimulation
} from "./api/api";

const emptySpeciesForm = {
  nome: "",
  tempBase: "",
  gddEmergenza: "",
  gddSviluppo: "",
  gddFioritura: "",
  gddFruttificazione: "",
  gddMaturazione: "",
  sogliaStressCaldo: "",
  sogliaStressFreddo: "",
  sensibilitaMalattie: "",
  noteAgronomiche: ""
};

function App() {
  const [species, setSpecies] = useState([]);
  const [plants, setPlants] = useState([]);
  const [gardens, setGardens] = useState([]);
  const [simulationResults, setSimulationResults] = useState([]);

  const [currentLocationId, setCurrentLocationId] = useState("");
  const [days, setDays] = useState(2);

  const [newSpecies, setNewSpecies] = useState(emptySpeciesForm);

  const [gardenSetup, setGardenSetup] = useState({
    nomeOrto: "",
    latitudine: "",
    longitudine: "",
  });

  const [newPlant, setNewPlant] = useState({
    locationId: "",
    specieId: "",
    nomePianta: "",
    note: ""
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    loadSpecies();
    loadGardens();
  }, []);

  async function loadSpecies() {
    resetFeedback();

    try {
      const data = await getSpecies();
      setSpecies(data);
    } catch (err) {
      setError(err.message);
    }
  }


  async function loadGardens() {
    resetFeedback();

    try {
      const data = await getGardens();
      setGardens(data);
    } catch (err) {
      setError(err.message);
    }
  }


  async function handleCreateSpecies(event) {
    event.preventDefault();
    resetFeedback();
    setLoading(true);

    try {
      const payload = {
        nome: newSpecies.nome,
        tempBase: toNumberOrNull(newSpecies.tempBase),
        gddEmergenza: toNumberOrNull(newSpecies.gddEmergenza),
        gddSviluppo: toNumberOrNull(newSpecies.gddSviluppo),
        gddFioritura: toNumberOrNull(newSpecies.gddFioritura),
        gddFruttificazione: toNumberOrNull(newSpecies.gddFruttificazione),
        gddMaturazione: toNumberOrNull(newSpecies.gddMaturazione),
        sogliaStressCaldo: toNumberOrNull(newSpecies.sogliaStressCaldo),
        sogliaStressFreddo: toNumberOrNull(newSpecies.sogliaStressFreddo),
        sensibilitaMalattie: newSpecies.sensibilitaMalattie,
        noteAgronomiche: newSpecies.noteAgronomiche
      };

      const created = await createSpecies(payload);

      setMessage(`Specie creata correttamente: ${created.nome}`);
      setNewSpecies(emptySpeciesForm);

      await loadSpecies();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }


  async function handleDeleteSpecies(specieId) {
    const confirmed = window.confirm("Vuoi eliminare questa specie?");

    if (!confirmed) {
      return;
    }

    resetFeedback();
    setLoading(true);

    try {
      await deleteSpecies(specieId);
      setMessage("Specie eliminata correttamente.");
      await loadSpecies();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }



  async function handleSetupGarden(event) {
    event.preventDefault();
    resetFeedback();
    setLoading(true);

    try {
      const payload = {
        nomeOrto: gardenSetup.nomeOrto,
        latitudine: toNumberOrNull(gardenSetup.latitudine),
        longitudine: toNumberOrNull(gardenSetup.longitudine),

      };

      const created = await createGarden(payload);

      setMessage(
          `Orto creato: ${created.nomeOrto}`
      );

      setCurrentLocationId(String(created.locationId));

      setNewPlant((prev) => ({
        ...prev,
        locationId: String(created.locationId)
      }));

      await loadGardens();
      await loadPlants(created.locationId);


      setGardenSetup({
        nomeOrto: "",
        latitudine: "",
        longitudine: ""
      });

    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }


  async function handleDeleteSimulation() {
    if (!currentLocationId) {
      setError("Seleziona prima un orto.");
      return;
    }

    const confirmed = window.confirm(
        "Vuoi eliminare la simulazione dal database per questo orto?"
    );

    if (!confirmed) {
      return;
    }

    resetFeedback();
    setLoading(true);

    try {
      await deleteGardenSimulation(currentLocationId);

      setSimulationResults([]);
      setMessage("Simulazione eliminata correttamente dal database.");

      await loadPlants(currentLocationId);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }




  async function handleAddPlant(event) {
    event.preventDefault();
    resetFeedback();
    setLoading(true);

    try {
      const payload = {
        locationId: toNumberOrNull(newPlant.locationId),
        specieId: toNumberOrNull(newPlant.specieId),
        nomePianta: newPlant.nomePianta,
        note: newPlant.note
      };

      const created = await addPlant(payload);

      setMessage(`Pianta aggiunta: ${created.nomePianta}`);

      setCurrentLocationId(String(created.locationId));

      setNewPlant((prev) => ({
        ...prev,
        nomePianta: "",
        note: ""
      }));

      await loadPlants(created.locationId);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleDeletePlant(plantId) {
    const confirmed = window.confirm("Vuoi eliminare questa pianta?");

    if (!confirmed) {
      return;
    }

    resetFeedback();
    setLoading(true);

    try {
      await deletePlant(plantId);
      setMessage("Pianta eliminata correttamente.");
      await loadPlants(currentLocationId);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }


  async function handleSelectGarden(locationId) {
    setCurrentLocationId(locationId);

    setNewPlant((prev) => ({
      ...prev,
      locationId: locationId
    }));

    setSimulationResults([]);

    if (!locationId) {
      setPlants([]);
      return;
    }

    await loadPlants(locationId);
  }

  async function loadPlants(locationId = currentLocationId) {
    resetFeedback();

    if (!locationId) {
      setError("Inserisci un locationId.");
      return;
    }

    setLoading(true);

    try {
      const data = await getPlantsByLocation(Number(locationId));

      setPlants(data);
      setCurrentLocationId(String(locationId));

      setNewPlant((prev) => ({
        ...prev,
        locationId: String(locationId)
      }));
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleRunSimulation(event) {
    event.preventDefault();
    resetFeedback();

    if (!currentLocationId) {
      setError("Seleziona prima un orto.");
      return;
    }

    setLoading(true);

    try {
      const data = await runGardenSimulation({
        locationId: Number(currentLocationId),
        giorni: Number(days)
      });


      setSimulationResults(data);
      setMessage("Simulazione completata correttamente.");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }


  function updateNewSpecies(field, value) {
    setNewSpecies((prev) => ({
      ...prev,
      [field]: value
    }));
  }



  function updateGardenSetup(field, value) {
    setGardenSetup((prev) => ({
      ...prev,
      [field]: value
    }));
  }



  function updateNewPlant(field, value) {
    setNewPlant((prev) => ({
      ...prev,
      [field]: value
    }));
  }


  function resetFeedback() {
    setMessage("");
    setError("");
  }


  function handleClearSimulationResults() {
    setSimulationResults([]);
    setMessage("Risultati simulazione cancellati dalla schermata.");
  }


  return (
      <div className="app">
        <header className="hero">
          <h1>Orto Smart</h1>
          <p>
            Gestisci specie, piante, coordinate, simulazione meteo, crescita,
            rischi e irrigazione.
          </p>
        </header>

        {loading && <div className="banner loading">Operazione in corso...</div>}
        {message && <div className="banner success">{message}</div>}
        {error && <div className="banner error">{error}</div>}

        <main className="layout">

          <SpeciesList
              species={species}
              onReload={loadSpecies}
              onDelete={handleDeleteSpecies}
              loading={loading}
          />

          <CreateSpeciesForm
              form={newSpecies}
              onChange={updateNewSpecies}
              onSubmit={handleCreateSpecies}
              loading={loading}
          />

          <CreateGardenForm
              form={gardenSetup}
              onChange={updateGardenSetup}
              onSubmit={handleSetupGarden}
              loading={loading}
          />

          <GardenPlants
              plants={plants}
              gardens={gardens}
              currentLocationId={currentLocationId}
              onSelectGarden={handleSelectGarden}
              onReloadPlants={loadPlants}
              onDeletePlant={handleDeletePlant}
              //onDeleteGarden={handleDeleteGarden}
              loading={loading}
          />

          <AddPlantForm
              form={newPlant}
              species={species}
              onChange={updateNewPlant}
              onSubmit={handleAddPlant}
              loading={loading}
          />

          <SimulationStart
              gardens={gardens}
              currentLocationId={currentLocationId}
              days={days}
              onSelectGarden={handleSelectGarden}
              onDaysChange={setDays}
              onRunSimulation={handleRunSimulation}
              loading={loading}
          />

          <SimulationResults
              results={simulationResults}
              onClear={handleClearSimulationResults}
              onDeleteSimulation={handleDeleteSimulation}
              loading={loading}
              hasSelectedGarden={!!currentLocationId}
          />
        </main>
      </div>
  );
}

function toNumberOrNull(value) {
  if (value === "" || value === null || value === undefined) {
    return null;
  }

  return Number(value);
}


export default App;
