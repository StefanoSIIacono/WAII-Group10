import { useCallback, useRef, useState } from 'react';
import { AsyncTypeahead } from 'react-bootstrap-typeahead';
import { searchExpertiseByName, searchProductsByName } from '../utils/Api';
import { ExpertiseDTO, ProductDTO } from '../types';

type Props = {
  type: 'products' | 'expertises';
  onSelect: (selected?: ProductDTO | ExpertiseDTO) => void;
};

export function SearchSelect({ type, onSelect }: Props) {
  const makeAndHandleRequest = type === 'products' ? searchProductsByName : searchExpertiseByName;

  const [isLoading, setIsLoading] = useState(false);
  const [options, setOptions] = useState<(ProductDTO | ExpertiseDTO)[]>([]);
  const [query, setQuery] = useState('');
  const [selected, setSelected] = useState<ProductDTO | ExpertiseDTO>();
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
        onSelect(option as unknown as ProductDTO | ExpertiseDTO);
        setSelected(option as unknown as ProductDTO | ExpertiseDTO);
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
        const item = option as ProductDTO | ExpertiseDTO;
        return (
          <div
            key={
              type == 'expertises' ? (item as ExpertiseDTO).field : (item as ProductDTO).productId
            }>
            <span>
              {type == 'expertises' ? (item as ExpertiseDTO).field : (item as ProductDTO).name}
            </span>
          </div>
        );
      }}
      useCache={false}
    />
  );
}
