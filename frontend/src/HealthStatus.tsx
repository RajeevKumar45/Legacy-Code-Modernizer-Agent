import { useEffect, useState } from 'react'

// The backend endpoint started with: uvicorn main:app --port 8000
const HEALTH_URL = 'http://127.0.0.1:8000/api/health'

// What we expect the backend to send back
type HealthResponse = {
  status: string
  service: string
  version: string
}

// The three situations the section can be in. Each one carries only the data it needs.
type HealthState =
  | { kind: 'loading' }
  | { kind: 'connected'; data: HealthResponse }
  | { kind: 'error'; message: string }

function HealthStatus() {
  // Start in "loading". Calling setHealth later makes React redraw with the new value.
  const [health, setHealth] = useState<HealthState>({ kind: 'loading' })

  // Run once after the component first appears on screen ([] = no dependencies).
  useEffect(() => {
    // Lets us cancel the request if the component disappears before it finishes
    const controller = new AbortController()

    async function checkHealth() {
      try {
        const response = await fetch(HEALTH_URL, { signal: controller.signal })

        // fetch only fails on network problems. A 404 or 500 still "succeeds",
        // so we must check the status code ourselves.
        if (!response.ok) {
          throw new Error(`Backend responded with HTTP ${response.status}.`)
        }

        const data = (await response.json()) as HealthResponse
        if (data.status !== 'ok') {
          throw new Error(`Backend reported status "${data.status}".`)
        }

        setHealth({ kind: 'connected', data })
      } catch (error) {
        // We cancelled on purpose, so this is not a real error
        if (controller.signal.aborted) return

        // A TypeError from fetch means the request never got a readable answer:
        // the server is off, the URL is wrong, or CORS blocked it.
        const message =
          error instanceof TypeError
            ? `Cannot reach the backend at ${HEALTH_URL}. Check that it is running and that CORS allows this page's origin.`
            : error instanceof Error
              ? error.message
              : 'Unknown error.'

        setHealth({ kind: 'error', message })
      }
    }

    checkHealth()

    // Cleanup: React calls this when the component is removed
    return () => controller.abort()
  }, [])

  return (
    // role="status" makes screen readers announce changes politely
    <section
      className={`health health-${health.kind}`}
      role="status"
      aria-labelledby="health-heading"
    >
      <h2 id="health-heading">Backend status</h2>
      {health.kind === 'loading' && <p>Loading…</p>}
      {health.kind === 'connected' && (
        <p>
          Connected to {health.data.service} (version {health.data.version}).
        </p>
      )}
      {health.kind === 'error' && <p>Not connected. {health.message}</p>}
    </section>
  )
}

export default HealthStatus
