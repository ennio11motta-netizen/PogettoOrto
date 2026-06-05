export default function SimulationResults({
                                              results,
                                              onClear,
                                          onDeleteSimulation,
                                          loading,
                                          hasSelectedGarden}) {
    return (
        <section className="card full-width">
            <div className="section-header">
                <h2>7. Risultati simulazione</h2>

                <div className="inline-actions">
                {results.length > 0 && (
                    <button type="button" onClick={onClear}>
                        Cancella risultati
                    </button>
                )}

                <button
                    type="button"
                    onClick={onDeleteSimulation}
                    disabled={loading || !hasSelectedGarden}
                    >
                    Elimina simulazione DB
                </button>

                </div>
            </div>

            {results.length === 0 && (
                <p>Nessun risultato disponibile.</p>
            )}

            {results.map((plantResult) => (
                <div className="result-block" key={plantResult.plantId}>
                    <h3>
                        {plantResult.nomePianta}{" "}
                        <span>#{plantResult.plantId}</span>
                    </h3>

                    <div className="table-wrapper">
                        <table>
                            <thead>
                            <tr>
                                <th>Data</th>
                                <th>Temp min</th>
                                <th>Temp max</th>
                                <th>Pioggia</th>
                                <th>Stadio</th>
                                <th>% ciclo</th>
                                <th>Rischio caldo</th>
                                <th>Rischio malattia</th>
                                <th>Irrigazione</th>
                                <th>Consiglio irrigazione</th>
                            </tr>
                            </thead>

                            <tbody>
                            {plantResult.risultati.map((step) => (
                                <tr key={`${plantResult.plantId}-${step.forecastId}`}>
                                    <td>{formatDate(step.date)}</td>
                                    <td>{step.tempMin}</td>
                                    <td>{step.tempMax}</td>
                                    <td>{step.precipitazione}</td>
                                    <td>{step.growthStage}</td>
                                    <td>{formatNumber(step.percentCiclo)}%</td>
                                    <td>
                                        <RiskBadge value={step.riskCaldo} />
                                    </td>
                                    <td>
                                        <RiskBadge value={step.riskMalattia} />
                                    </td>
                                    <td>
                                        <IrrigationBadge value={step.irrigationLevel} />
                                    </td>
                                    <td>{step.irrigationAdvice}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            ))}
        </section>
    );
}

function formatNumber(value) {
    if (value === null || value === undefined) {
        return "-";
    }

    return Number(value).toFixed(2);
}

function formatDate(value) {
    if (!value) {
        return "-";
    }

    return value.replace("T", " ").slice(0, 16);
}

function RiskBadge({ value }) {
    const className = `badge risk-${value?.toLowerCase() || "unknown"}`;

    return (
        <span className={className}>
      {value || "-"}
    </span>
    );
}

function IrrigationBadge({ value }) {
    const className = `badge irrigation-${value?.toLowerCase() || "unknown"}`;

    return (
        <span className={className}>
      {value || "-"}
    </span>
    );
}