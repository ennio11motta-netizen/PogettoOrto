export default function SimulationResults({
                                              results,
                                              onClear,
                                              onPreviewSimulation,
                                              onApplySimulation,
                                              loading,
                                              hasSelectedGarden
                                          }) {
    return (
        <section className="card full-width">
            <div className="section-header">
                <h2>7. Previsione / simulazione crescita</h2>

                <div className="inline-actions">
                    <button
                        type="button"
                        onClick={onPreviewSimulation}
                        disabled={loading || !hasSelectedGarden}
                    >
                        Genera previsione
                    </button>


                    <button
                        type="button"
                        onClick={() => {
                            console.log("Click Applica simulazione");
                            onApplySimulation();
                        }}
                        disabled={loading || !hasSelectedGarden}
                    >
                        Applica simulazione
                    </button>


                    {results.length > 0 && (
                        <button
                            type="button"
                            onClick={onClear}
                            disabled={loading}
                        >
                            Cancella risultati
                        </button>
                    )}
                </div>
            </div>

            {results.length === 0 && (
                <p>
                    Nessun risultato disponibile. Genera una previsione oppure applica una
                    simulazione.
                </p>
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
                                <th>GDD accumulati</th>
                                <th>% ciclo</th>
                                <th>Rischio caldo</th>
                                <th>Rischio malattia</th>
                                <th>Irrigazione</th>
                                <th>Consiglio irrigazione</th>
                            </tr>
                            </thead>

                            <tbody>
                            {plantResult.risultati.map((step, index) => (
                                <tr key={`${plantResult.plantId}-${step.date}-${index}`}>
                                    <td>{formatDate(step.date)}</td>
                                    <td>{formatNumber(step.tempMin)}</td>
                                    <td>{formatNumber(step.tempMax)}</td>
                                    <td>{formatNumber(step.precipitazione)}</td>
                                    <td>{step.growthStage || "-"}</td>
                                    <td>{formatNumber(step.storeGDD)}</td>
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

                                    <td>{step.irrigationAdvice || "-"}</td>
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
    if (value === null || value === undefined || Number.isNaN(Number(value))) {
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