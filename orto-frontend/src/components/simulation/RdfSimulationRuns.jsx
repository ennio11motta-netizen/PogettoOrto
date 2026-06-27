export default function RdfSimulationRuns({
                                              gardens,
                                              currentLocationId,
                                              rdfSimulationHistory,
                                              onSelectGarden,
                                              onLoadHistory,
                                              loading
                                          }) {
    return (
        <section className="card full-width">
            <div className="section-header">
                <h2>8. Storico RDF simulazioni</h2>

                <div className="inline-actions">
                    <button
                        type="button"
                        onClick={() => onLoadHistory(currentLocationId)}
                        disabled={loading || !currentLocationId}
                    >
                        Carica storico RDF
                    </button>
                </div>
            </div>

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
            </div>

            {(!rdfSimulationHistory || rdfSimulationHistory.length === 0) && (
                <p>
                    Nessuno storico RDF disponibile per questo orto. Esegui una
                    simulazione oppure clicca su “Carica storico RDF”.
                </p>
            )}

            {rdfSimulationHistory &&
                rdfSimulationHistory.map((run) => (
                    <div className="result-block" key={run.simulationUri}>
                        <div className="section-header">
                            <h3>
                                SimulationRun{" "}
                                <span>#{run.runId || extractRunId(run.simulationUri)}</span>
                            </h3>

                            <span className={`badge mode-${run.mode?.toLowerCase() || "unknown"}`}>
                {run.mode || "-"}
              </span>
                        </div>

                        <div className="rdf-summary">
                            <p>
                                <strong>URI simulazione:</strong>{" "}
                                {run.simulationUri || "-"}
                            </p>

                            <p>
                                <strong>Creata il:</strong>{" "}
                                {formatDateTime(run.createdAt)}
                            </p>

                            <p>
                                <strong>Orto RDF:</strong>{" "}
                                {run.gardenUri || "-"}
                            </p>
                        </div>

                        <RdfPlantsSection plants={run.plants} />

                        <RdfWeatherSection weatherDays={run.weatherDays} />

                        <RdfRisksSection risks={run.risks} />

                        <RdfForecastsSection forecasts={run.forecasts} />
                    </div>
                ))}
        </section>
    );
}

function RdfPlantsSection({ plants }) {
    return (
        <div className="rdf-section">
            <h4>Piante coinvolte</h4>

            {(!plants || plants.length === 0) && (
                <p>Nessuna pianta collegata a questa simulazione.</p>
            )}

            {plants && plants.length > 0 && (
                <div className="mini-list">
                    {plants.map((plant) => (
                        <div className="mini-card" key={plant.plantUri}>
                            <strong>{plant.name || shortUri(plant.plantUri)}</strong>

                            <span>URI: {plant.plantUri}</span>
                            <span>Specie: {plant.speciesName || "-"}</span>
                            <span>Stadio: {plant.growthStage || "-"}</span>
                            <span>GDD accumulati: {formatNumber(plant.storeGDD)}</span>
                            <span>Note: {plant.note || "-"}</span>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

function RdfWeatherSection({ weatherDays }) {
    return (
        <div className="rdf-section">
            <h4>Meteo collegati</h4>

            {(!weatherDays || weatherDays.length === 0) && (
                <p>Nessun meteo collegato a questa simulazione.</p>
            )}

            {weatherDays && weatherDays.length > 0 && (
                <div className="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Data</th>
                            <th>Temp min</th>
                            <th>Temp max</th>
                            <th>Umidità</th>
                            <th>Pioggia</th>
                            <th>Vento</th>
                            <th>UV</th>
                            <th>URI</th>
                        </tr>
                        </thead>

                        <tbody>
                        {weatherDays.map((weather) => (
                            <tr key={weather.weatherUri}>
                                <td>{formatDateTime(weather.dateTime)}</td>
                                <td>{formatNumber(weather.tempMin)}</td>
                                <td>{formatNumber(weather.tempMax)}</td>
                                <td>{formatNumber(weather.humidity)}</td>
                                <td>{formatNumber(weather.precipitation)}</td>
                                <td>{formatNumber(weather.windKmh)}</td>
                                <td>{formatNumber(weather.uvIndex)}</td>
                                <td>{shortUri(weather.weatherUri)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

function RdfRisksSection({ risks }) {
    return (
        <div className="rdf-section">
            <h4>Rischi prodotti</h4>

            {(!risks || risks.length === 0) && (
                <p>Nessun rischio collegato a questa simulazione.</p>
            )}

            {risks && risks.length > 0 && (
                <div className="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Pianta</th>
                            <th>Caldo</th>
                            <th>Freddo</th>
                            <th>Vento</th>
                            <th>Malattia</th>
                            <th>Consigli</th>
                        </tr>
                        </thead>

                        <tbody>
                        {risks.map((risk) => (
                            <tr key={risk.riskUri}>
                                <td>
                                    <strong>
                                        {risk.plantName || shortUri(risk.plantUri) || "-"}
                                    </strong>
                                    <br />
                                    <small>Giorno: {formatDateOnly(risk.dateTime)}</small>
                                </td>

                                <td>
                                    <RiskBadge value={risk.riskCaldo} />
                                </td>

                                <td>
                                    <RiskBadge value={risk.riskFreddo} />
                                </td>

                                <td>
                                    <RiskBadge value={risk.riskVento} />
                                </td>

                                <td>
                                    <RiskBadge value={risk.riskMalattia} />
                                </td>

                                <td>{risk.consigli || "-"}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

function RdfForecastsSection({ forecasts }) {
    return (
        <div className="rdf-section">
            <h4>Forecast prodotti</h4>

            {(!forecasts || forecasts.length === 0) && (
                <p>Nessun forecast collegato a questa simulazione.</p>
            )}

            {forecasts && forecasts.length > 0 && (
                <div className="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Pianta</th>
                            <th>Data</th>
                            <th>GDD giornaliero</th>
                            <th>% ciclo</th>
                            <th>Stadio</th>
                            <th>Giorni maturazione</th>
                            <th>Forecast</th>
                        </tr>
                        </thead>

                        <tbody>
                        {forecasts.map((forecast) => (
                            <tr key={forecast.forecastUri}>
                                <td>
                                    <strong>
                                        {forecast.plantName || shortUri(forecast.plantUri) || "-"}
                                    </strong>
                                    <br />
                                    <small>Pianta: {shortUri(forecast.plantUri)}</small>
                                </td>

                                <td>{formatDateTime(forecast.dateTime)}</td>
                                <td>{formatNumber(forecast.gddDaily)}</td>
                                <td>{formatNumber(forecast.percentCiclo)}%</td>
                                <td>{forecast.growthStage || "-"}</td>
                                <td>{forecast.daysToMaturity ?? "-"}</td>
                                <td>{shortUri(forecast.forecastUri)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

function RiskBadge({ value }) {
    const className = `badge risk-${value?.toLowerCase() || "unknown"}`;

    return (
        <span className={className}>
      {value || "-"}
    </span>
    );
}

function extractRunId(simulationUri) {
    if (!simulationUri) {
        return "-";
    }

    return simulationUri.split("/").pop();
}

function shortUri(uri) {
    if (!uri) {
        return "-";
    }

    return uri.split("/").pop();
}

function formatDateTime(value) {
    if (!value) {
        return "-";
    }

    return value.replace("T", " ").slice(0, 19);
}

function formatNumber(value) {
    if (value === null || value === undefined || Number.isNaN(Number(value))) {
        return "-";
    }
    return Number(value).toFixed(2);
}

function formatDateOnly(value) {
    if (!value) {
        return "-";
    }

    return value.split("T")[0];
}
