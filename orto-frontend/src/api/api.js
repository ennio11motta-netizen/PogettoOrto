const API_BASE_URL = "http://localhost:8080/api";

async function request(endpoint, options = {}) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    let data = null;

    try {
        data = await response.json();
    } catch {
        data = null;
    }

    if (!response.ok) {
        const message =
            data?.message ||
            data?.error ||
            `Errore HTTP ${response.status}`;

        throw new Error(message);
    }

    return data;
}

export function getSpecies() {
    return request("/species");
}

export function createSpecies(payload) {
    return request("/species", {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

export function setupGarden(payload) {
    return request("/garden/setup", {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

export function addPlant(payload) {
    return request("/plants", {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

export function getPlantsByLocation(locationId) {
    return request(`/plants?locationId=${locationId}`);
}

export function runGardenSimulation(payload) {
    return request("/simulation/run-garden", {
        method: "POST",
        body: JSON.stringify(payload)
    });
}