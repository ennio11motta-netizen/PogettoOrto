export default function SimulationStart({
    gardens,
    currentLocationId,
    days,
    onSelectGarden,
    onDaysChange,
    onRunSimulation,
    loading
                                        }) {
    return (
        <section className="card">
            <h2>6. Simula orto</h2>

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

                <input
                    type="number"
                    min="1"
                    max="16"
                    value={days}
                    onChange={(e) => onDaysChange(e.target.value)}
                    placeholder="Giorni"
                />

                <button
                    type="button"
                    onClick={onRunSimulation}
                    disabled={loading || !currentLocationId}
                >
                    Avvia simulazione
                </button>
            </div>
        </section>
    )
}