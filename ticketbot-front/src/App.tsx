import { useState, type FormEvent } from 'react'
import './App.css'

interface Ticket {
  id: number;
  title: string;
  description: string;
  category: string;    
  assignedTeam: string; 
  status: string;      
}

function App() {
  const [title, setTitle] = useState<string>('')
  const [description, setDescription] = useState<string>('')
  
  const [createdTicket, setCreatedTicket] = useState<Ticket | null>(null)
  
  const [loading, setLoading] = useState<boolean>(false)

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setCreatedTicket(null)

    const ticketData = {
      title: title,
      description: description
    }

    try {
      const response = await fetch('http://localhost:8080/api/tickets', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(ticketData),
      })

      if (response.ok) {
        const data: Ticket = await response.json()
        setCreatedTicket(data)
        
        setTitle('')
        setDescription('')
      } else {
        alert('Erro ao criar ticket! Verifique o console.')
      }
    } catch (error) {
      console.error('Erro de conexão:', error)
      alert('Erro ao conectar com o backend. O Java está rodando?')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="meu-container-principal">
      <h1>TicketBot AI</h1>
      <p>Descreva seu problema e nossa IA fará a triagem automática.</p>

      <div className="card-form">
        <form onSubmit={handleSubmit}>
          
          <div className="form-group">
            <label><strong>Título:</strong></label>
            <input 
              className="form-input"
              type="text" 
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Ex: Monitor piscando" 
              required 
            />
          </div>

          <div className="form-group">
            <label><strong>Descrição Detalhada:</strong></label>
            <textarea 
              className="form-input"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Ex: O monitor fica piscando intermitentemente..." 
              rows={4} 
              required 
            />
          </div>

          {}
          <button type="submit" className="btn-exemplo" disabled={loading}>
            {loading ? 'Analisando com IA...' : 'Enviar Ticket'}
          </button>
        </form>
      </div>

      {createdTicket && (
        <div className="result-box">
          <h3>✅ Ticket Criado com Sucesso!</h3>
          <p><strong>ID:</strong> #{createdTicket.id}</p>
          <p><strong>Título:</strong> {createdTicket.title}</p>
          <hr />
          <p>
            <strong>Classificação IA:</strong> 
            <span className="tag tag-ia">{createdTicket.category}</span>
          </p>
          <p>
            <strong>Equipe Responsável:</strong> 
            <span className="tag tag-team">{createdTicket.assignedTeam}</span>
          </p>
          <p><strong>Status:</strong> {createdTicket.status}</p>
        </div>
      )}
    </div>
  )
}

export default App