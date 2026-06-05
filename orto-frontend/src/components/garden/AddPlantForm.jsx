export default function AddPlantForm({
    form,
    species,
    onChange,
    onSubmit,
    loading
                                     }) {
    return (
        <section className="card">
            <h2>5. Aggiungi pianta all'orto</h2>

            <form className="form-grid" onSubmit={onSubmit}>
                <label>
                    Location ID
                    <input
                        value={form.locationId}
                        onChange={(e) =>
                            onChange("locationId", e.target.value)
                        }
                        required
                    />
                </label>

                <label>
                    Specie
                    <select
                        value={form.specieId}
                        onChange={(e) => onChange("specieId", e.target.value)}
                        required
                    >
                        <option value="">Seleziona specie</option>
                        {species.map((specie) => (
                            <option key={specie.specieId} value={specie.specieId}>
                                {specie.nome} - ID {specie.specieId}
                            </option>
                        ))}
                    </select>
                </label>

                <label>
                    Nome pianta
                    <input
                        value={form.nomePianta}
                        onChange={(e) =>
                            onChange("nomePianta", e.target.value)
                        }
                        placeholder="Es. Basilico vaso 1"
                        required
                    />
                </label>

                <label className="full">
                    Note
                    <textarea
                        value={form.note}
                        onChange={(e) => onChange("note", e.target.value)}
                    />
                </label>

                <button type="submit" disabled={loading}>
                    Aggiungi pianta
                </button>
            </form>
        </section>
    )
}