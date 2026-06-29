export default function SimulationStart({
                                            gardens,
                                            currentLocationId,
                                            days,
                                            onSelectGarden,
                                            onDaysChange,
                                            loading
                                        }) {
    return (
        <section className="card">
            <div className="section-header">
                <h2>6. Impostazioni simulazione</h2>
            </div>

            <p>
                Seleziona un orto e il numero di giorni da usare per la previsione o
                per la simulazione applicata.
            </p>

            <div className="inline-form">
                <select
                    value={currentLocationId}
                    onChange={(event) => onSelectGarden(event.target.value)}
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
                    value={days}
                    onChange={(event) => onDaysChange(event.target.value)}
                    disabled={loading}
                    placeholder="Giorni"
                />
            </div>

            <small>
                Usa la Card 7 per generare una previsione oppure applicare davvero la
                simulazione.
            </small>
        </section>
    );
}