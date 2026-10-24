import './App.css'
import HealthStatus from './HealthStatus'

// The shape every workflow stage must follow. TypeScript checks this for us.
type Stage = {
  name: string
  description: string
}

// The four planned stages, in order. Plain data for now, no logic behind them yet.
const stages: Stage[] = [
  {
    name: 'Analyze',
    description:
      'Read the legacy code to understand its structure, dependencies, and risky areas.',
  },
  {
    name: 'Modernize',
    description:
      'Rewrite the code using current language features, libraries, and practices.',
  },
  {
    name: 'Test',
    description:
      'Check that the modernized code still behaves the same as the original.',
  },
  {
    name: 'Compare',
    description:
      'Show the original and modernized code side by side so changes are easy to review.',
  },
]

function App() {
  return (
    <main className="page">
      <header className="intro">
        <h1>Legacy Code Modernizer Agent</h1>
        <p>
          A tool that helps you turn old, hard-to-maintain code into clean,
          modern code, while making sure it still works the same way.
        </p>
      </header>

      <HealthStatus />

      <section aria-labelledby="workflow-heading">
        <h2 id="workflow-heading">Planned workflow</h2>
        {/* An ordered list, because the stages happen in sequence */}
        <ol className="stages">
          {stages.map((stage, index) => (
            <li key={stage.name} className="stage">
              <span className="stage-number" aria-hidden="true">
                {index + 1}
              </span>
              <h3>{stage.name}</h3>
              <p>{stage.description}</p>
            </li>
          ))}
        </ol>
      </section>
    </main>
  )
}

export default App
