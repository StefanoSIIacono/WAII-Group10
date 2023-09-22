import { useCallback, useRef, useState } from 'react';
import { AsyncTypeahead } from 'react-bootstrap-typeahead';
import { searchExpertiseByName, searchExpertsByName, searchProductsByName } from '../utils/Api';
import { ExpertDTO, ExpertiseDTO, ProductDTO } from '../types';

type Props = {
  type: 'products' | 'expertises' | 'experts';
  onSelect: (selected?: ProductDTO | ExpertiseDTO | ExpertDTO) => void;
};

export function SearchSelect({ type, onSelect }: Props) {
  const makeAndHandleRequest =
    type === 'products'
      ? searchProductsByName
      : type === 'expertises'
      ? searchExpertiseByName
      : searchExpertsByName;

  const [isLoading, setIsLoading] = useState(false);
  const [options, setOptions] = useState<(ProductDTO | ExpertiseDTO | ExpertDTO)[]>([]);
  const [query, setQuery] = useState('');
  const [selected, setSelected] = useState<ProductDTO | ExpertiseDTO | ExpertDTO>();
  const [showRequiredError, setShowRequiredError] = useState(false);

  const page = useRef(1);

  const handleInputChange = (text: string) => {
    setQuery(text);
    onSelect(undefined);
    setSelected(undefined);
    page.current = 1;
  };

  const handlePagination = async () => {
    setIsLoading(true);

    const nextPage = page.current + 1;

    const result = await makeAndHandleRequest(query, nextPage);

    if (result.success && result.data?.data) {
      setIsLoading(false);
      setOptions(options.concat(result.data.data));
      page.current = nextPage;
    }

    return;
  };

  const handleSearch = useCallback(async (query: string) => {
    setIsLoading(true);
    const result = await makeAndHandleRequest(query);
    setIsLoading(false);

    if (result.success && result.data?.data) {
      setIsLoading(false);
      setOptions(result.data.data);
    }

    page.current = 1;
  }, []);

  return (
    <AsyncTypeahead
      id="searchSelect"
      isLoading={isLoading}
      labelKey="login"
      maxResults={100}
      inputProps={{
        style: showRequiredError ? { borderColor: 'red', color: 'red' } : {}
      }}
      onChange={(option) => {
        onSelect(option as unknown as ProductDTO | ExpertiseDTO | ExpertDTO);
        setSelected(option as unknown as ProductDTO | ExpertiseDTO | ExpertDTO);
        setShowRequiredError(false);
      }}
      minLength={2}
      onInputChange={handleInputChange}
      onPaginate={handlePagination}
      onSearch={handleSearch}
      onBlur={() => {
        if (selected === undefined) {
          setShowRequiredError(true);
        }
      }}
      options={options}
      paginate
      placeholder={`Select ${type}`}
      renderMenuItemChildren={(option) => {
        const item = option as ProductDTO | ExpertiseDTO | ExpertDTO;
        return (
          <div
            key={
              type === 'expertises'
                ? (item as ExpertiseDTO).field
                : type === 'products'
                ? (item as ProductDTO).productId
                : (item as ExpertDTO).email
            }>
            <span>
              {type === 'expertises'
                ? (item as ExpertiseDTO).field
                : type === 'products'
                ? (item as ProductDTO).name
                : (item as ExpertDTO).email}
            </span>
          </div>
        );
      }}
      useCache={false}
    />
  );
}
