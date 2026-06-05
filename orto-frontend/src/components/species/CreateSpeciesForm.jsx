export default function CreateSpeciesForm({
                                              form,
                                              onChange,
                                              onSubmit,
                                              loading
                                          }) {
    return (
        <section className="card">
            <h2>2. Crea nuova specie</h2>

            <form className="form-grid" onSubmit={onSubmit}>
                <label>
                    Nome specie
                    <input
                        value={form.nome}
                        onChange={(e) => onChange("nome", e.target.value)}
                        placeholder="Es. Basilico"
                        required
                    />
                </label>

                <label>
                    Temperatura base
                    <input
                        type="number"
                        step="0.1"
                        value={form.tempBase}
                        onChange={(e) => onChange("tempBase", e.target.value)}
                        placeholder="Es. 12"
                        required
                    />
                </label>

                <label>
                    GDD emergenza
                    <input
                        type="number"
                        step="0.1"
                        value={form.gddEmergenza}
                        onChange={(e) =>
                            onChange("gddEmergenza", e.target.value)
                        }
                        placeholder="Es. 60"
                        required
                    />
                </label>

                <label>
                    GDD sviluppo
                    <input
                        type="number"
                        step="0.1"
                        value={form.gddSviluppo}
                        onChange={(e) =>
                            onChange("gddSviluppo", e.target.value)
                        }
                        placeholder="Es. 250"
                        required
                    />
                </label>

                <label>
                    GDD fioritura
                    <input
                        type="number"
                        step="0.1"
                        value={form.gddFioritura}
                        onChange={(e) =>
                            onChange("gddFioritura", e.target.value)
                        }
                        placeholder="Es. 500"
                        required
                    />
                </label>

                <label>
                    GDD fruttificazione
                    <input
                        type="number"
                        step="0.1"
                        value={form.gddFruttificazione}
                        onChange={(e) =>
                            onChange("gddFruttificazione", e.target.value)
                        }
                        placeholder="Es. 700"
                        required
                    />
                </label>

                <label>
                    GDD maturazione
                    <input
                        type="number"
                        step="0.1"
                        value={form.gddMaturazione}
                        onChange={(e) =>
                            onChange("gddMaturazione", e.target.value)
                        }
                        placeholder="Es. 900"
                        required
                    />
                </label>

                <label>
                    Soglia stress caldo
                    <input
                        type="number"
                        step="0.1"
                        value={form.sogliaStressCaldo}
                        onChange={(e) =>
                            onChange("sogliaStressCaldo", e.target.value)
                        }
                        placeholder="Es. 30"
                        required
                    />
                </label>

                <label>
                    Soglia stress freddo
                    <input
                        type="number"
                        step="0.1"
                        value={form.sogliaStressFreddo}
                        onChange={(e) =>
                            onChange("sogliaStressFreddo", e.target.value)
                        }
                        placeholder="Es. 7"
                        required
                    />
                </label>

                <label>
                    Sensibilità malattie
                    <input
                        value={form.sensibilitaMalattie}
                        onChange={(e) =>
                            onChange("sensibilitaMalattie", e.target.value)
                        }
                        placeholder="Es. Media"
                    />
                </label>

                <label className="full">
                    Note agronomiche
                    <textarea
                        value={form.noteAgronomiche}
                        onChange={(e) =>
                            onChange("noteAgronomiche", e.target.value)
                        }
                        placeholder="Es. Richiede irrigazione regolare."
                    />
                </label>

                <button type="submit" disabled={loading}>
                    Crea specie
                </button>
            </form>
        </section>
    );
}