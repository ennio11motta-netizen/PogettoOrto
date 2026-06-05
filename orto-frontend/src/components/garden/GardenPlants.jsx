export default function  GardenPlants({
    plants,
    gardens,
    currentLocationId,
    onSelectGarden,
    onReloadPlants,
    onDeletePlant,
    onDeleteGarden,
    loading
                                      }) {
    return (
        <section className="card">
            <h2>4. Piante dell'orto</h2>

            <div className="inline-form">
                <select
                    value={currentLocationId}
                    onChange={(e) => onSelectGarden(e.target.value)}
                    disabled={loading}
                >
                    <option value="">Seleziona un orto</option>

                    {gardens.map((garden) => (
                        <option key={garden.locationId} value={garden.locationId}>
                            {garden.nomeOrto} - ID {garden.locationId}
                        </option>
                    ))}
                </select>

                <button
                    type="button"
                    onClick={() => onReloadPlants(currentLocationId)}
                    disabled={loading || !currentLocationId}
                >
                    Ricarica piante
                </button>


                <button
                    type="button"
                    onClick={() => onDeleteGarden(currentLocationId)}
                    disabled={loading || !currentLocationId}
                >
                    Elimina orto
                </button>

            </div>

            <div className="mini-list">
                {plants.length === 0 && <p>Nessuna pianta caricata.</p>}

                {plants.map((plant) => (
                    <div className="mini-card" key={plant.plantId}>
                        <strong>{plant.nomePianta}</strong>


                        <button
                            type="button"
                            onClick={() => onDeletePlant(plant.plantId)}
                            disabled={loading}
                        >
                            Elimina
                        </button>


                        <span>ID pianta: {plant.plantId}</span>
                        <span>Specie: {plant.specieNome}</span>
                        <span>Stadio: {plant.growthStage}</span>
                        <span>GDD accumulati: {plant.storeGDD}</span>
                        <span>Note: {plant.note || "-"}</span>

                    </div>
                ))}
            </div>
        </section>
    )
}