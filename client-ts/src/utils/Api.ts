import {
  ApiResponse,
  LoginDTO,
  CreateProfileDTO,
  CreateExpertDTO,
  MeDTO,
  PagedDTO,
  ExpertDTO,
  ExpertiseDTO,
  MessageDTO,
  UnreadMessagesDTO,
  BodyMessageDTO,
  ProductDTO,
  ProfileDTO,
  TicketDTO,
  ChangeProfileInfoDTO,
  TicketCreateBodyDTO,
  TicketInProgressBodyDTO,
  Priority,
  Status,
  MeDTOFromApi,
  Roles,
  StatsDTO
} from '../types';

const URL = 'http://localhost:8080';

// TODO: add better error descriptions

// Security

export async function login(loginDTO: LoginDTO) {
  return post<LoginDTO, string>('/login', loginDTO);
}

export async function logout() {
  return post('/user/logout');
}

export async function signup(createProfileDTO: CreateProfileDTO) {
  return post<CreateProfileDTO>('/signup', createProfileDTO);
}

export async function createExpert(
  createExpertDTO: CreateExpertDTO
): Promise<ApiResponse<undefined>> {
  return post<CreateExpertDTO>('/expert', createExpertDTO);
}

export async function getMe(): Promise<ApiResponse<MeDTO>> {
  const request = await get<MeDTOFromApi>('/me');

  const result: ApiResponse<MeDTO> = {
    ...request,
    data: request.data
      ? {
          ...request.data,
          role: Roles[request.data.role as keyof typeof Roles]
        }
      : undefined
  };

  return result;
}

// Expert

export async function getExperts(page?: number, offset?: number) {
  return getPaginated<ExpertDTO>('/experts', page, offset);
}

export async function searchExpertsByNameAndExpertise(
  name: string,
  expertise?: string,
  page?: number,
  offset?: number
) {
  return getPaginated<ProductDTO>(
    `/experts/search/${name}${expertise ? `?expertise=${expertise}` : ''}`,
    page,
    offset
  );
}

export async function getExpert(email: string) {
  return get<ExpertDTO>(`/expert/${email}`);
}

export async function addExpertiseToExpert(email: string, expertise: ExpertiseDTO) {
  return put<ExpertiseDTO>(`/experts/${email}/expertise`, expertise);
}

export async function deleteExpertiseFromExpert(email: string, expertise: ExpertiseDTO) {
  return del<ExpertiseDTO>(`/experts/${email}/expertise`, expertise);
}

export async function getExpertStats(email: string) {
  return get<StatsDTO>(`/experts/${email}/stats`);
}

// Expertise

export async function getExpertises(page?: number, offset?: number) {
  return getPaginated<ExpertiseDTO>('/expertises', page, offset);
}

export async function searchExpertiseByName(name: string, page?: number, offset?: number) {
  return getPaginated<ExpertiseDTO>(`/expertises/search/${name}`, page, offset);
}

export async function getExpertsByExpertises(field: string, page?: number, offset?: number) {
  return getPaginated<ExpertDTO>(`/expertises/${field}/experts`, page, offset);
}

export async function createExpertise(expertise: ExpertiseDTO) {
  return post<ExpertiseDTO>('/expertises', expertise);
}

export async function deleteExpertise(field: string) {
  return del<ExpertiseDTO>(`/expertises/${field}`);
}

// Message

export async function getTicketMessages(ticketId: number, page?: number, offset?: number) {
  return getPaginated<MessageDTO>(`/tickets/${ticketId}/messages`, page, offset);
}

export async function getUnreadMessages(page?: number, offset?: number) {
  return getPaginated<UnreadMessagesDTO>('/messages/unread', page, offset);
}

export async function addMessage(ticketId: number, bodyMessageDTO: BodyMessageDTO) {
  return post<BodyMessageDTO>(`/tickets/${ticketId}/messages`, bodyMessageDTO);
}

export async function ackMessage(ticketId: number, index?: number) {
  return index === undefined
    ? put(`/tickets/${ticketId}/messages/ack`)
    : put(`/tickets/${ticketId}/messages/${index}/ack`);
}

// Products

export async function getProducts(page?: number, offset?: number) {
  return getPaginated<ProductDTO>('/products', page, offset);
}

export async function searchProductsByName(name: string, page?: number, offset?: number) {
  return getPaginated<ProductDTO>(`/products/search/${name}`, page, offset);
}

export async function getProduct(productId: string) {
  return get<ProductDTO>(`/products/${productId}`);
}

// Profile

export async function getProfiles(page?: number, offset?: number) {
  return getPaginated<ProfileDTO>('/profiles', page, offset);
}

export async function getProfile(email: string) {
  return get<ProfileDTO>(`/profiles/${email}`);
}

export async function getTicketsByEmail(email: string, page?: number, offset?: number) {
  return getPaginated<TicketDTO>(`/profiles/${email}/tickets`, page, offset);
}

export async function editProfile(changeProfileInfoDTO: ChangeProfileInfoDTO) {
  return put<ChangeProfileInfoDTO>('/profiles/edit', changeProfileInfoDTO);
}

// Tickets

export async function getTickets(page?: number, offset?: number) {
  return getPaginated<TicketDTO>('/tickets', page, offset);
}

export async function getTicket(ticketId: number) {
  return get<TicketDTO>(`/tickets/${ticketId}`);
}

export async function createTicket(ticketCreateBodyDTO: TicketCreateBodyDTO) {
  return post<TicketCreateBodyDTO>('/tickets', ticketCreateBodyDTO);
}

export async function openTicket(ticketId: number) {
  return put(`/tickets/${ticketId}/open`);
}

export async function reOpenTicket(ticketId: number) {
  return put(`/tickets/${ticketId}/reopen`);
}

export async function closeTicket(ticketId: number) {
  return put(`/tickets/${ticketId}/close`);
}

export async function resolveTicket(ticketId: number) {
  return put(`/tickets/${ticketId}/resolved`);
}

export async function setTicketStatus(
  ticketId: number,
  status: Status,
  ticketInProgressBodyDTO?: TicketInProgressBodyDTO
) {
  switch (status) {
    case Status.OPEN: {
      return openTicket(ticketId);
    }
    case Status.CLOSED: {
      return closeTicket(ticketId);
    }
    case Status.IN_PROGRESS: {
      if (!ticketInProgressBodyDTO) {
        throw new Error('Specify expert and priority');
      }
      return inProgessTicket(ticketId, ticketInProgressBodyDTO);
    }
    case Status.REOPENED: {
      return reOpenTicket(ticketId);
    }
    case Status.RESOLVED: {
      return resolveTicket(ticketId);
    }
  }
}

export async function inProgessTicket(
  ticketId: number,
  ticketInProgressBodyDTO: TicketInProgressBodyDTO
) {
  return put<TicketInProgressBodyDTO>(`/tickets/${ticketId}/inprogress`, ticketInProgressBodyDTO);
}

export async function setTicketPriority(ticketId: number, priority: Priority) {
  return put(`/tickets/${ticketId}/priority/${priority}`);
}

// helpers

async function getPaginated<T>(
  path: string,
  page?: number,
  offset?: number
): Promise<ApiResponse<PagedDTO<T>>> {
  try {
    let paramsString = '';
    const params = [];
    if (page) {
      params.push(`page=${page}`);
    }
    if (offset) {
      params.push(`offset=${offset}`);
    }
    if (params.length > 0) {
      paramsString = `?${params.join('&')}`;
    }
    const response = await fetch(`${URL}${path}${paramsString}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
      signal: AbortSignal.timeout(100),
      credentials: 'include'
    });

    if (response.ok) {
      const data: PagedDTO<T> = await response.json();
      return {
        success: true,
        statusCode: response.status,
        data
      };
    }

    return {
      success: false,
      statusCode: response.status,
      error: response.statusText
    };
  } catch (e) {
    if (e instanceof DOMException) {
      return {
        success: false,
        statusCode: -50,
        error: ''
      };
    }
  }

  return {
    success: false,
    statusCode: -1,
    error: 'network error'
  };
}

async function get<T>(path: string): Promise<ApiResponse<T>> {
  try {
    console.log(`${URL}${path}`);
    const response = await fetch(`${URL}${path}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
      signal: AbortSignal.timeout(100),
      credentials: 'include'
    });

    if (response.ok) {
      const data: T = await response.json();
      return {
        success: true,
        statusCode: response.status,
        data
      };
    }

    return {
      success: false,
      statusCode: response.status,
      error: response.statusText
    };
  } catch (e) {
    //
  }

  return {
    success: false,
    statusCode: -1,
    error: 'network error'
  };
}

async function post<S = undefined, T = undefined>(
  path: string,
  body?: S,
  content = false
): Promise<ApiResponse<T>> {
  try {
    const response = await fetch(`${URL}${path}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      signal: AbortSignal.timeout(100),
      credentials: 'include',
      body: body ? JSON.stringify(body) : undefined
    });

    if (response.ok) {
      return {
        success: true,
        statusCode: response.status,
        data: content ? await response.json() : undefined
      };
    }

    return {
      success: false,
      statusCode: response.status,
      error: response.statusText
    };
  } catch (e) {
    //
  }

  return {
    success: false,
    statusCode: -1,
    error: 'network error'
  };
}

async function put<S = undefined, T = undefined>(
  path: string,
  body?: S,
  content = false
): Promise<ApiResponse<T>> {
  try {
    const response = await fetch(`${URL}${path}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      signal: AbortSignal.timeout(100),
      credentials: 'include',
      body: body ? JSON.stringify(body) : undefined
    });

    if (response.ok) {
      return {
        success: true,
        statusCode: response.status,
        data: content ? await response.json() : undefined
      };
    }

    return {
      success: false,
      statusCode: response.status,
      error: response.statusText
    };
  } catch (e) {
    //
  }

  return {
    success: false,
    statusCode: -1,
    error: 'network error'
  };
}

async function del<S = undefined, T = undefined>(
  path: string,
  body?: S,
  content = false
): Promise<ApiResponse<T>> {
  try {
    const response = await fetch(`${URL}${path}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
      signal: AbortSignal.timeout(100),
      credentials: 'include',
      body: body ? JSON.stringify(body) : undefined
    });

    if (response.ok) {
      return {
        success: true,
        statusCode: response.status,
        data: content ? await response.json() : undefined
      };
    }

    return {
      success: false,
      statusCode: response.status,
      error: response.statusText
    };
  } catch (e) {
    //
  }

  return {
    success: false,
    statusCode: -1,
    error: 'network error'
  };
}
