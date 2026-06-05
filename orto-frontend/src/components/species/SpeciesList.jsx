export default function SpeciesList({ species, onReload, loading, onDelete }) {
    return (
        <section className="card">
            <h2>1. Specie disponibili</h2>

            <button type="button" onClick={onReload} disabled={loading}>
                Aggiorna specie
            </button>

            <div className="mini-list">
                {species?.length === 0 && <p>Nessuna specie disponibile.</p>}

                {species?.map((specie) => (
                    <div className="mini-card" key={specie.specieId}>
                        <strong>{specie.nome}</strong>


                        <button
                            type="button"
                            onClick={() => onDelete(specie.specieId)}
                            disabled={loading}
                        >
                            Elimina
                        </button>



                        <span>ID specie: {specie.specieId}</span>
                        <span>Temperatura base: {specie.tempBase}</span>

                        <span>GDD emergenza: {specie.gddEmergenza}</span>
                        <span>GDD sviluppo: {specie.gddSviluppo}</span>
                        <span>GDD fioritura: {specie.gddFioritura}</span>
                        <span>GDD fruttificazione: {specie.gddFruttificazione}</span>
                        <span>GDD maturazione: {specie.gddMaturazione}</span>

                        <span>Soglia stress caldo: {specie.sogliaStressCaldo}</span>
                        <span>Soglia stress freddo: {specie.sogliaStressFreddo}</span>

                        <span>Sensibilità malattie: {specie.sensibilitaMalattie || "-"}</span>
                        <span>Note agronomiche: {specie.noteAgronomiche || "-"}</span>
                    </div>
                ))}
            </div>
        </section>
    );
}