export default function CreateGardenForm({
    form,
    onChange,
    onSubmit,
    loading
}) {
    return(
        <section className="card">
            <h2>3. Crea orto</h2>

            <form className="form-grid" onSubmit={onSubmit}>
                <label>
                    Nome orto
                    <input
                        value={form.nomeOrto}
                        onChange={(e) =>
                            onChange("nomeOrto", e.target.value)
                        }
                        placeholder="Es. Orto di casa"
                        required
                    />
                </label>

                <label>
                    Latitudine
                    <input
                        type="number"
                        step="0.0001"
                        value={form.latitudine}
                        onChange={(e) =>
                            onChange("latitudine", e.target.value)
                        }
                        placeholder="Es. 43.1417"
                        required
                    />
                </label>

                <label>
                    Longitudine
                    <input
                        type="number"
                        step="0.0001"
                        value={form.longitudine}
                        onChange={(e) =>
                            onChange("longitudine", e.target.value)
                        }
                        placeholder="Es. 12.2056"
                        required
                    />
                </label>


                <button type="submit" disabled={loading}>
                    Crea orto
                </button>
            </form>
        </section>
    )
}