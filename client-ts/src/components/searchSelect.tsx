import { ForwardedRef, forwardRef, useCallback, useRef, useState } from 'react';
import { AsyncTypeahead } from 'react-bootstrap-typeahead';
import {
  searchExpertiseByName,
  searchExpertsByNameAndExpertise,
  searchProductsByName
} from '../utils/Api';
import { ExpertDTO, ExpertiseDTO, ProductDTO } from '../types';
import Typeahead from 'react-bootstrap-typeahead/types/core/Typeahead';

type Props = {
  type: 'products' | 'expertises' | 'experts';
  expertise?: string;
  onSelect: (selected?: ProductDTO | ExpertiseDTO | ExpertDTO) => void;
  elementsToFilter?: string[];
  allowNewElement?: boolean;
};

type Option<T> = Partial<T> & { new: boolean };

export const SearchSelect = forwardRef(function SearchSelect(
  { type, onSelect, elementsToFilter, allowNewElement, expertise }: Props,
  ref: ForwardedRef<Typeahead>
) {
  const makeAndHandleRequest = type === 'products' ? searchProductsByName : searchExpertiseByName;

  const [isLoading, setIsLoading] = useState(false);
  const [options, setOptions] = useState<
    (Option<ProductDTO> | Option<ExpertiseDTO> | Option<ExpertDTO>)[]
  >([]);
  const [selected, setSelected] = useState<ProductDTO | ExpertiseDTO | ExpertDTO>();
  const [showRequiredError, setShowRequiredError] = useState(false);
  const [query, setQuery] = useState('');

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

    const result = await (type === 'experts'
      ? searchExpertsByNameAndExpertise(query, expertise, nextPage)
      : makeAndHandleRequest(query, nextPage));

    if (result.success && result.data?.data) {
      setIsLoading(false);
      setOptions(options.concat(result.data.data.map((e) => ({ ...e, new: false }))));
      page.current = nextPage;
    }

    return;
  };

  const handleSearch = useCallback(async (query: string) => {
    setIsLoading(true);

    const result = await (type === 'experts'
      ? searchExpertsByNameAndExpertise(query, expertise)
      : makeAndHandleRequest(query));

    setIsLoading(false);

    if (result.success && result.data?.data) {
      setIsLoading(false);
      setOptions(result.data.data.map((e) => ({ ...e, new: false })));
    }

    page.current = 1;
  }, []);

  return (
    <AsyncTypeahead
      id="searchSelect"
      isLoading={isLoading}
      filterBy={(option) =>
        elementsToFilter
          ? !elementsToFilter?.includes(
              type === 'expertises'
                ? (option as ExpertiseDTO).field
                : type === 'products'
                ? (option as ProductDTO).name
                : (option as ExpertDTO).email
            )
          : true
      }
      labelKey={(option) =>
        type === 'expertises'
          ? (option as ExpertiseDTO).field
          : type === 'products'
          ? (option as ProductDTO).name
          : (option as ExpertDTO).email
      }
      maxResults={100}
      inputProps={{
        style: showRequiredError ? { borderColor: 'red', color: 'red' } : {}
      }}
      onChange={(option) => {
        onSelect(option[0] as unknown as ProductDTO | ExpertiseDTO | ExpertDTO);
        setSelected(option[0] as unknown as ProductDTO | ExpertiseDTO | ExpertDTO);
        setShowRequiredError(false);
      }}
      minLength={allowNewElement ? 0 : 2}
      onInputChange={handleInputChange}
      onPaginate={handlePagination}
      onSearch={handleSearch}
      onBlur={() => {
        if (selected === undefined) {
          setShowRequiredError(true);
        }
      }}
      ref={ref}
      options={[
        ...(allowNewElement && query.length > 0
          ? [{ name: query, email: query, field: query, new: true }]
          : []),
        ...options
      ]}
      paginate
      placeholder={`Select ${type}`}
      renderMenuItemChildren={(option) => {
        const item = option as Option<ProductDTO> | Option<ExpertiseDTO> | Option<ExpertDTO>;
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
              {!item.new
                ? type === 'expertises'
                  ? (item as ExpertiseDTO).field
                  : type === 'products'
                  ? (item as ProductDTO).name
                  : (item as ExpertDTO).email
                : `Add: ${(item as Option<ExpertDTO>).name}`}
            </span>
          </div>
        );
      }}
      useCache={false}
    />
  );
});
